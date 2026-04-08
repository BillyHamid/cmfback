package com.adouk.finacombackend.Application.services.Impl;

import com.adouk.finacombackend.domain.aggregates.ConfigMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final SesClient sesClient;

    private static final String FROM_ADDRESS = "FINACOM BURKINA <contact@finacom.bf>";

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        Destination destination = Destination.builder()
                .toAddresses(to)
                .build();

        Content subjectContent = Content.builder()
                .data(subject)
                .build();

        Body body = Body.builder()
                .html(Content.builder().data(htmlContent).build())
                .build();

        Message message = Message.builder()
                .subject(subjectContent)
                .body(body)
                .build();

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .source(FROM_ADDRESS)
                .destination(destination)
                .message(message)
                .build();

        sesClient.sendEmail(emailRequest);
    }

    public void sendWelcomePasswordEmail(String name, String to, String username, String password) {
        log.info("Sending email to {}", to);
        String subject = ConfigMap.OBJET_MAIL_ACCESS;

        String htmlTemplate = """
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); overflow: hidden;">

          <!-- Header -->
          <div style="background-color: #247039; padding: 30px 20px; text-align: center;">
            <h1 style="margin: 0; color: #ffffff; font-size: 24px;">
              Bienvenue sur <span style="color: #eab309;">FINACOM+</span>
            </h1>
            <p style="color: #e0f2e9; font-size: 13px; margin-top: 8px;">
              Votre espace est maintenant accessible
            </p>
          </div>

          <!-- Content -->
          <div style="padding: 30px 20px;">
            <p style="font-size: 16px; color: #333333; margin-bottom: 20px;">
              {name},
            </p>

            <p style="font-size: 16px; color: #333333;">
              Voici vos identifiants de connexion :
            </p>

            <table style="width: 100%; margin-top: 20px; margin-bottom: 30px; font-size: 16px;">
              <tbody>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Nom d’utilisateur :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{username}</td>
                </tr>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Mot de passe :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{password}</td>
                </tr>
              </tbody>
            </table>

            <p style="font-size: 14px; color: #777; margin-top: 40px; line-height: 1.6;">
              Pour votre sécurité, la modification de votre mot de passe vous sera demandée à votre première connexion.<br />
              Cet email est généré automatiquement, merci de ne pas y répondre.
            </p>
          </div>

          <!-- Footer -->
          <div style="background-color: #f1f1f1; padding: 16px 20px; text-align: center; font-size: 13px; color: #888;">
            &copy; 2025 FINACOM - Tous droits réservés
          </div>

        </div>
        """;

        String htmlContent = htmlTemplate
                .replace("{name}", escapeHtml(name))
                .replace("{username}", escapeHtml(username))
                .replace("{password}", escapeHtml(password));

        Destination destination = Destination.builder().toAddresses(to).build();
        Message message = Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder()
                        .html(Content.builder().data(htmlContent).build())
                        .build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(FROM_ADDRESS)
                .destination(destination)
                .message(message)
                .build();

        System.out.println(sesClient.sendEmail(request));
    }

    public void sendResetPasswordEmail(String name, String to, String username, String password) {
        log.info("Sending email to {}", to);
        String subject = ConfigMap.OBJET_MAIL_ACCESS;

        String htmlTemplate = """
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); overflow: hidden;">

          <!-- Header -->
          <div style="background-color: #247039; padding: 30px 20px; text-align: center;">
            <h1 style="margin: 0; color: #ffffff; font-size: 24px;">
              Reset de mode passe sur <span style="color: #eab309;">FINACOM+</span>
            </h1>
            <p style="color: #e0f2e9; font-size: 13px; margin-top: 8px;">
              Votre espace est maintenant accessible
            </p>
          </div>

          <!-- Content -->
          <div style="padding: 30px 20px;">
            <p style="font-size: 16px; color: #333333; margin-bottom: 20px;">
              {name},
            </p>

            <p style="font-size: 16px; color: #333333;">
              Voici vos identifiants de connexion :
            </p>

            <table style="width: 100%; margin-top: 20px; margin-bottom: 30px; font-size: 16px;">
              <tbody>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Nom d’utilisateur :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{username}</td>
                </tr>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Mot de passe :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{password}</td>
                </tr>
              </tbody>
            </table>

            <p style="font-size: 14px; color: #777; margin-top: 40px; line-height: 1.6;">
              Pour votre sécurité, la modification de votre mot de passe vous sera demandée à votre première connexion.<br />
              Cet email est généré automatiquement, merci de ne pas y répondre.
            </p>
          </div>

          <!-- Footer -->
          <div style="background-color: #f1f1f1; padding: 16px 20px; text-align: center; font-size: 13px; color: #888;">
            &copy; 2025 FINACOM - Tous droits réservés
          </div>

        </div>
        """;

        String htmlContent = htmlTemplate
                .replace("{name}", escapeHtml(name))
                .replace("{username}", escapeHtml(username))
                .replace("{password}", escapeHtml(password));

        Destination destination = Destination.builder().toAddresses(to).build();
        Message message = Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder()
                        .html(Content.builder().data(htmlContent).build())
                        .build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(FROM_ADDRESS)
                .destination(destination)
                .message(message)
                .build();

        System.out.println(sesClient.sendEmail(request));
    }

    public void sendWelcome(String name, String username, String password, String to) {
        log.info("Sending welcome email to {}", to);
        String subject = ConfigMap.OBJET_MAIL_VALIDATION;

        String htmlTemplate = """
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); overflow: hidden;">
        
          <!-- Header -->
          <div style="background-color: #247039; padding: 30px 20px; text-align: center;">
            <h1 style="margin: 0; color: #ffffff; font-size: 24px;">
              Félicitations <span style="color: #eab309;">{name}</span> 🎉
            </h1>
            <p style="color: #e0f2e9; font-size: 13px; margin-top: 8px;">
              Votre demande a été validée avec succès
            </p>
          </div>
        
          <!-- Content -->
          <div style="padding: 30px 20px;">
            <p style="font-size: 16px; color: #333333; margin-bottom: 20px;">
              Nous avons le plaisir de vous informer que votre dossier d'ouverture de compte FINACOM a été validé et que vous pouvez désormais vous connecter à votre espace client sur FINACOM+.
            </p>
        
            <p style="font-size: 16px; color: #333333;">
             Veuillez trouver ci-dessous les informations nécessaires pour vous connecter a l'application FINACOM+.
            </p>
          </div>
          <table style="width: 90%; margin:auto; margin-top: -20px; margin-bottom: 30px; font-size: 16px;">
              <tbody>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Nom d’utilisateur :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{username}</td>
                </tr>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Mot de passe :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{password}</td>
                </tr>
              </tbody>
            </table>
        
          <!-- Footer -->
          <div style="background-color: #f1f1f1; padding: 20px; text-align: center; font-size: 13px; color: #555;">
            📞 Service client : <strong>+226 25 36 25 56</strong><br/>
            ✉️ Email : <a href="mailto:contact@finacom.bf" style="color: #247039;">contact@finacom.bf</a><br/><br/>
            &copy; 2025 FINACOM - Tous droits réservés
          </div>
        
        </div>
        """;


        String htmlContent = htmlTemplate.replace("{name}", escapeHtml(name)).replace("{username}", escapeHtml(username)).replace("{password}", escapeHtml(password));

        Destination destination = Destination.builder().toAddresses(to).build();
        Message message = Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder()
                        .html(Content.builder().data(htmlContent).build())
                        .build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(FROM_ADDRESS)
                .destination(destination)
                .message(message)
                .build();

        System.out.println(sesClient.sendEmail(request));
    }


    public void sendReleveRequestNotif(String name, String clientName, String numeroCompte, String to) {
        log.info("Sending welcome email to {}", to);
        String subject = ConfigMap.OBJET_MAIL_VALIDATION;

        String htmlTemplate = """
        <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); overflow: hidden;">
        
          <!-- Header -->
          <div style="background-color: #247039; padding: 30px 20px; text-align: center;">
            <h1 style="margin: 0; color: #ffffff; font-size: 24px;">
              NOTIFICATION DEMANDE DE RELEVES
            </h1>
            <p style="color: #e0f2e9; font-size: 13px; margin-top: 8px;">
              Demande de relevé de compte en attente de traitement
            </p>
          </div>
        
          <!-- Content -->
          <div style="padding: 30px 20px;">
          <p style="font-size: 16px; color: #333333; margin-bottom: 20px;">
              {name},
            </p>
            <p style="font-size: 16px; color: #333333; margin-bottom: 20px;">
              Nous vous informons que vous avez une nouvelle demande de relevé de compte en attente de traitement.
            </p>
            <table style="width: 100%; margin-top: 20px; margin-bottom: 30px; font-size: 16px;">
              <tbody>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Client :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{clientName}</td>
                </tr>
                <tr>
                  <td style="padding: 8px 0; color: #555;">
                    <strong>Numéro de compte :</strong>
                  </td>
                  <td style="padding: 8px 0; color: #111;">{numeroCompte}</td>
                </tr>
              </tbody>
            </table>
          </div>
          
        
            <p style="font-size: 16px; color: #333333; margin-bottom: 20px;">
              Veuillez vous connecter sur FINACOM+ pour traiter la demande.
            </p>
          <!-- Footer -->
          <div style="background-color: #f1f1f1; padding: 20px; text-align: center; font-size: 13px; color: #555;">
            📞 Service client : <strong>+226 25 36 25 56</strong><br/>
            ✉️ Email : <a href="mailto:contact@finacom.bf" style="color: #247039;">contact@finacom.bf</a><br/><br/>
            &copy; 2025 FINACOM - Tous droits réservés
          </div>
        
        </div>
        """;


        String htmlContent = htmlTemplate.replace("{name}", escapeHtml(name)).replace("{clientName}", escapeHtml(clientName)).replace("{numeroCompte}", escapeHtml(numeroCompte));

        Destination destination = Destination.builder().toAddresses(to).build();
        Message message = Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder()
                        .html(Content.builder().data(htmlContent).build())
                        .build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(FROM_ADDRESS)
                .destination(destination)
                .message(message)
                .build();

        System.out.println(sesClient.sendEmail(request));
    }

    private static String escapeHtml(String input) {
        return input == null ? "" : input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}

