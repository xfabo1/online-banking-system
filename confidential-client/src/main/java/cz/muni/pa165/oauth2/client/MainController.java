package cz.muni.pa165.oauth2.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public MainController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/")
    public String index(
            Model model,
            @AuthenticationPrincipal OidcUser user
    ) {
        model.addAttribute("user", user);
        if (user != null) {
            OAuth2AuthorizedClient oauth2Client = authorizedClientService.loadAuthorizedClient("muni",
                                                                                               user.getName()
            );
            if (oauth2Client != null) {
                String accessToken = oauth2Client.getAccessToken().getTokenValue();
                model.addAttribute("token", accessToken);
            } else {
                model.addAttribute("token", "Not available");
            }
        }
        return "index";
    }

    @GetMapping("/mycalendar")
    public String index() {
        //redirect to index page
        return "redirect:/";
    }


}