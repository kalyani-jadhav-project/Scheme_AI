package com.krushimitra.app.scheduler;

import com.krushimitra.app.entity.GovernmentScheme;
import com.krushimitra.app.entity.Notification;
import com.krushimitra.app.repository.GovernmentSchemeRepository;
import com.krushimitra.app.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Periodically checks PIB (Press Information Bureau) press releases for
 * mentions of our government schemes, and creates an admin notification
 * when a possible update is found. This does NOT auto-edit scheme data —
 * a human should always verify before changing figures like subsidy amounts.
 */
@Component
public class SchemeUpdateWatcher {

    private static final Logger logger = LoggerFactory.getLogger(SchemeUpdateWatcher.class);

    // PIB's official "All Press Releases" RSS feed (covers every ministry;
    // we filter by scheme keywords below since there's no agriculture-only feed).
    private static final String PIB_RSS_URL = "https://pib.gov.in/RssMain.aspx?ModId=6&Lang=1&Regid=1";

    @Autowired private GovernmentSchemeRepository schemeRepository;
    @Autowired private NotificationRepository notificationRepository;

    // Tracks links we've already notified about during this run of the app,
    // so we don't spam duplicate notifications every time the job fires.
    private final Set<String> alreadyNotifiedLinks = new HashSet<>();

    /**
     * Runs once a day at 8:00 AM server time.
     * Cron format: second minute hour day month weekday
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void checkForSchemeUpdates() {
        logger.info("SchemeUpdateWatcher: checking PIB press releases for scheme-related news...");

        List<GovernmentScheme> activeSchemes = schemeRepository.findAll()
                .stream()
                .filter(GovernmentScheme::isActive)
                .toList();

        if (activeSchemes.isEmpty()) {
            logger.info("SchemeUpdateWatcher: no active schemes to check against, skipping.");
            return;
        }

        List<PressItem> items;
        try {
            items = fetchPressReleases();
        } catch (Exception e) {
            logger.error("SchemeUpdateWatcher: failed to fetch/parse PIB RSS feed: {}", e.getMessage());
            return;
        }

        int newAlerts = 0;
        for (PressItem item : items) {
            if (alreadyNotifiedLinks.contains(item.link())) {
                continue;
            }
            for (GovernmentScheme scheme : activeSchemes) {
                if (mentionsScheme(item.title(), scheme)) {
                    createAdminNotification(scheme, item);
                    alreadyNotifiedLinks.add(item.link());
                    newAlerts++;
                    break; // one alert per press item is enough
                }
            }
        }

        logger.info("SchemeUpdateWatcher: checked {} press releases, created {} new alert(s).",
                items.size(), newAlerts);
    }

    private boolean mentionsScheme(String title, GovernmentScheme scheme) {
        if (title == null) return false;
        String lowerTitle = title.toLowerCase();
        String code = scheme.getSchemeCode() != null ? scheme.getSchemeCode().toLowerCase() : "";
        String name = scheme.getName() != null ? scheme.getName().toLowerCase() : "";
        return (!code.isBlank() && lowerTitle.contains(code))
                || (!name.isBlank() && lowerTitle.contains(name.split("\\(")[0].trim()));
    }

    private void createAdminNotification(GovernmentScheme scheme, PressItem item) {
        Notification notification = new Notification();
        notification.setTitle("Possible update: " + scheme.getName());
        notification.setMessage("PIB press release mentions this scheme: \"" + item.title() + "\". "
                + "Please review and update scheme details if needed. Source: " + item.link());
        notification.setType(Notification.NotificationType.ALERT);
        notification.setGlobal(true);
        notification.setRelatedSchemeId(scheme.getId());
        notificationRepository.save(notification);
    }

    private List<PressItem> fetchPressReleases() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PIB_RSS_URL))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new java.io.ByteArrayInputStream(response.body().getBytes()));

        NodeList itemNodes = doc.getElementsByTagName("item");
        List<PressItem> items = new java.util.ArrayList<>();
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Element el = (Element) itemNodes.item(i);
            String title = textOf(el, "title");
            String link = textOf(el, "link");
            if (title != null) {
                items.add(new PressItem(title, link));
            }
        }
        return items;
    }

    private String textOf(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() == 0) return null;
        return nodes.item(0).getTextContent();
    }

    private record PressItem(String title, String link) {}
}