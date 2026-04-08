package com.adouk.finacombackend.domain.beans;

import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.*;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.infrastructure.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Charge des données fictives pour la démo (profil demo uniquement).
 */
@Component
@Profile("demo")
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class DemoDataService implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${app.demo.commercial.password:commercial123}")
    private String commercialPassword;

    private final AgenceRepository agenceRepository;
    private final ServicesRepository servicesRepository;
    private final FoirAQuestionRepository foirAQuestionRepository;
    private final DocumentRequisRepository documentRequisRepository;
    private final ProspectClientPhysiqueRepository prospectClientPhysiqueRepository;
    private final ProspectClientMoralRepository prospectClientMoralRepository;
    private final ProspectClientAssociationRepository prospectClientAssociationRepository;
    private final DossierClientRepository dossierClientRepository;
    private final ClientPhysiqueRepository clientPhysiqueRepository;
    private final ClientMoralRepository clientMoralRepository;
    private final ClientAssociationRepository clientAssociationRepository;
    private final CompteBancaireRepository compteBancaireRepository;
    private final ReleveCompteRepository releveCompteRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (agenceRepository.count() > 0) {
            log.info("Données démo déjà présentes, skip.");
            ensureCommercialPassword();
            return;
        }
        log.info("Chargement des données fictives pour la démo...");
        loadAgences();
        loadServices();
        loadFaq();
        loadDocumentsRequis();
        loadDossiersEtProspects();
        loadClients();
        loadReleves();
        loadCommercialUser();
        ensureCommercialPassword();
        log.info("Données fictives chargées avec succès.");
    }

    private void loadCommercialUser() {
        List<Agence> agences = agenceRepository.findAll();
        if (agences.isEmpty()) return;
        if (userRepository.findByUsername("commercial").isPresent()) return;
        if (roleRepository.findByCode("agent-commercial") == null) return;
        UserCommand cmd = new UserCommand();
        cmd.setFirstname("Jean");
        cmd.setLastname("Commercial");
        cmd.setUsername("commercial");
        cmd.setEmail("commercial@coris.bf");
        cmd.setPhone("+226 70 99 88 77");
        cmd.setRole("agent-commercial");
        cmd.setAgenceId(agences.get(0).getUuid());
        userService.createAgent(cmd);
    }

    /** Garantit que le mot de passe commercial est commercial123 en démo */
    private void ensureCommercialPassword() {
        userRepository.findByUsername("commercial").ifPresent(user -> {
            String expected = (commercialPassword != null && !commercialPassword.isEmpty()) ? commercialPassword : "commercial123";
            if (!passwordEncoder.matches(expected, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(expected));
                userRepository.save(user);
                log.info("Mot de passe commercial réinitialisé pour la démo");
            }
        });
    }

    private void loadAgences() {
        String[][] agences = {
            {"Agence Ouagadougou Centre", "Avenue Kwame N'Krumah, Ouagadougou", "+226 25 33 12 34", "+226 25 33 12 35"},
            {"Agence Bobo-Dioulasso", "Rue de la Paix, Bobo-Dioulasso", "+226 20 97 12 34", null},
            {"Agence Koudougou", "Place du Marché, Koudougou", "+226 25 44 56 78", null}
        };
        for (String[] a : agences) {
            Agence ag = new Agence();
            ag.setAgencyName(a[0]);
            ag.setAgencyAdress(a[1]);
            ag.setAgencyPhone1(a[2]);
            ag.setAgencyPhone2(a[3]);
            ag.setActive(true);
            agenceRepository.save(ag);
        }
    }

    private void loadServices() {
        String[][] services = {
            {"CC", "Compte Courant", "Compte à vue pour les opérations courantes"},
            {"CE", "Compte Épargne", "Compte d'épargne rémunéré"},
            {"CT", "Compte à Terme", "Dépôt à terme avec intérêts"}
        };
        for (String[] s : services) {
            Services svc = new Services();
            svc.setCode(s[0]);
            svc.setLibelleService(s[1]);
            svc.setDescriptionService(s[2]);
            servicesRepository.save(svc);
        }
    }

    private void loadFaq() {
        String[][] faqs = {
            {"Comment ouvrir un compte ?", "Rendez-vous dans une agence avec une pièce d'identité et un justificatif de domicile."},
            {"Quels sont les frais de tenue de compte ?", "Les frais varient selon le type de compte. Consultez notre grille tarifaire."},
            {"Comment demander un relevé de compte ?", "Connectez-vous à votre espace client ou contactez votre agence."}
        };
        for (String[] f : faqs) {
            FoirAQuestion faq = new FoirAQuestion();
            faq.setLibelleQuestion(f[0]);
            faq.setReponseQuestion(f[1]);
            foirAQuestionRepository.save(faq);
        }
    }

    private void loadDocumentsRequis() {
        Object[][] docs = {
            {"CNIB", "Carte nationale d'identité", "PHYSIQUE", DocumentDestinator.PERSONNE_PHYSIQUE},
            {"Justificatif de domicile", "Facture ou attestation de moins de 3 mois", "PHYSIQUE", DocumentDestinator.PERSONNE_PHYSIQUE},
            {"RCCM", "Registre de commerce", "MORALE", DocumentDestinator.PERSONNE_MORALE},
            {"Statuts", "Statuts de la société", "MORALE", DocumentDestinator.PERSONNE_MORALE},
            {"Procès-verbal", "PV de l'assemblée constitutive", "ASSOCIATION", DocumentDestinator.ASSOCIATION}
        };
        for (Object[] d : docs) {
            DocumentRequis doc = new DocumentRequis();
            doc.setLibelle((String) d[0]);
            doc.setDescription((String) d[1]);
            doc.setType((String) d[2]);
            doc.setDestinator((DocumentDestinator) d[3]);
            documentRequisRepository.save(doc);
        }
    }

    private void loadDossiersEtProspects() {
        // Prospect physique 1
        ProspectClientPhysique p1 = new ProspectClientPhysique();
        p1.setNom("Traoré");
        p1.setPrenom("Fatou");
        p1.setDateNaissance(LocalDate.of(1990, 5, 15));
        p1.setLieuNaissance("Ouagadougou");
        p1.setProfession("Comptable");
        p1.setEmployeur("SARL TechBurkina");
        p1.setNumeroCNIB("BF-123456789");
        p1.setAdresse("Secteur 15, Ouagadougou");
        p1.setEmail("fatou.traore@email.com");
        p1.setTelephone1("+226 70 12 34 56");
        p1 = prospectClientPhysiqueRepository.save(p1);

        DossierClient d1 = new DossierClient();
        d1.setProspectClient(p1);
        d1.setTypeClient(ClientType.PHYSIQUE);
        d1.setStatutDossier(StatusDossierClient.PENDING);
        d1.setDateSoumission(LocalDateTime.now().minusDays(2));
        dossierClientRepository.save(d1);

        // Prospect physique 2
        ProspectClientPhysique p2 = new ProspectClientPhysique();
        p2.setNom("Sawadogo");
        p2.setPrenom("Moussa");
        p2.setDateNaissance(LocalDate.of(1985, 8, 20));
        p2.setLieuNaissance("Bobo-Dioulasso");
        p2.setProfession("Commerçant");
        p2.setNumeroCNIB("BF-987654321");
        p2.setAdresse("Quartier Konsa, Bobo-Dioulasso");
        p2.setEmail("moussa.sawadogo@email.com");
        p2.setTelephone1("+226 76 98 76 54");
        p2 = prospectClientPhysiqueRepository.save(p2);

        DossierClient d2 = new DossierClient();
        d2.setProspectClient(p2);
        d2.setTypeClient(ClientType.PHYSIQUE);
        d2.setStatutDossier(StatusDossierClient.PENDING);
        d2.setDateSoumission(LocalDateTime.now().minusDays(1));
        dossierClientRepository.save(d2);

        // Prospect moral
        ProspectClientMoral pm = new ProspectClientMoral();
        pm.setRaisonSociale("SARL Agro-Burkina");
        pm.setRccm("BF-OUA-2024-001");
        pm.setNumIfu("00001234");
        pm.setFormeJuridique("SARL");
        pm.setDomaineActivite("Agriculture");
        pm.setNomResponsable("Ouédraogo");
        pm.setPrenomResponsable("Jean");
        pm.setSexeResponsable("Masculin");
        pm.setTelResponsable("+226 70 11 22 33");
        pm.setEmailResponsable("contact@agro-burkina.bf");
        pm.setAdresse("Zone industrielle, Ouagadougou");
        pm = prospectClientMoralRepository.save(pm);

        DossierClient d3 = new DossierClient();
        d3.setProspectClient(pm);
        d3.setTypeClient(ClientType.MORALE);
        d3.setStatutDossier(StatusDossierClient.PENDING);
        d3.setDateSoumission(LocalDateTime.now().minusDays(3));
        dossierClientRepository.save(d3);

        // Prospect association
        ProspectClientAssociation pa = new ProspectClientAssociation();
        pa.setNomOrganisation("Coopérative des Éleveurs du Centre");
        pa.setTypeOrganisation("Coopérative");
        pa.setDomaineActivite("Élevage");
        pa.setNomRepresentant("Kaboré");
        pa.setPrenomRepresentant("Aminata");
        pa.setSexeRepresentant("Féminin");
        pa.setTelRepresentant("+226 76 55 44 33");
        pa.setEmailRepresentant("coop.eleveurs@email.com");
        pa.setAdresse("Koudougou");
        pa = prospectClientAssociationRepository.save(pa);

        DossierClient d4 = new DossierClient();
        d4.setProspectClient(pa);
        d4.setTypeClient(ClientType.ASSOCIATION);
        d4.setStatutDossier(StatusDossierClient.PENDING);
        d4.setDateSoumission(LocalDateTime.now().minusDays(5));
        dossierClientRepository.save(d4);
    }

    private void loadClients() {
        List<Agence> agences = agenceRepository.findAll();
        if (agences.isEmpty()) return;
        Agence agence = agences.get(0);

        // Client physique
        ClientPhysique cp = new ClientPhysique();
        cp.setNom("Kaboré");
        cp.setPrenom("Abdoul");
        cp.setDateNaissance(LocalDate.of(1988, 3, 10));
        cp.setLieuNaissance("Ouagadougou");
        cp.setProfession("Ingénieur");
        cp.setEmployeur("Ministère des Finances");
        cp.setNumeroCNIB("BF-111222333");
        cp.setNumeroClient("CLI-PH-001");
        cp.setAdresse("Secteur 10, Ouagadougou");
        cp.setEmail("abdoul.kabore@email.com");
        cp.setTelephone1("+226 70 00 11 22");
        cp.setKycStatus(ClientStatus.VALIDE);
        cp.setKycValidated(true);
        cp.setAgenceRattachee(agence);
        cp = clientPhysiqueRepository.save(cp);

        CompteBancaire cb1 = new CompteBancaire();
        cb1.setClient(cp);
        cb1.setNumeroCompte("BF00123456789012345678901234");
        cb1.setLibelle("Compte principal");
        cb1.setTypeCompte("CC");
        cb1.setStatut("ACTIF");
        cb1.setCodeBanque("10101");
        cb1.setCodeGuichet("00001");
        cb1.setCleRib("12");
        cb1.setIban("BF08 10101 00001 12345678901234 12");
        compteBancaireRepository.save(cb1);

        // Client moral
        ClientMoral cm = new ClientMoral();
        cm.setRaisonSociale("SA Burkina Telecom");
        cm.setRccm("BF-OUA-2020-056");
        cm.setNumIfu("00005678");
        cm.setFormeJuridique("SA");
        cm.setDomaineActivite("Télécommunications");
        cm.setNomResponsable("Zoungrana");
        cm.setPrenomResponsable("Paul");
        cm.setSexeResponsable("Masculin");
        cm.setTelResponsable("+226 25 30 40 50");
        cm.setEmailResponsable("direction@burkinatelecom.bf");
        cm.setAdresseResponsable("Avenue de la Nation, Ouagadougou");
        cm.setNumeroClient("CLI-MO-001");
        cm.setAdresse("Avenue de la Nation, Ouagadougou");
        cm.setEmail("contact@burkinatelecom.bf");
        cm.setTelephone1("+226 25 30 40 50");
        cm.setKycStatus(ClientStatus.VALIDE);
        cm.setKycValidated(true);
        cm.setAgenceRattachee(agence);
        cm = clientMoralRepository.save(cm);

        CompteBancaire cb2 = new CompteBancaire();
        cb2.setClient(cm);
        cb2.setNumeroCompte("BF00987654321098765432109876");
        cb2.setLibelle("Compte exploitation");
        cb2.setTypeCompte("CC");
        cb2.setStatut("ACTIF");
        cb2.setIban("BF08 10101 00001 98765432109876 34");
        compteBancaireRepository.save(cb2);

        // Client association
        ClientAssociation ca = new ClientAssociation();
        ca.setNomOrganisation("Association des Jeunes Entrepreneurs");
        ca.setTypeOrganisation("Association");
        ca.setDomaineActivite("Formation");
        ca.setNomRepresentant("Diallo");
        ca.setPrenomRepresentant("Mariam");
        ca.setSexeRepresentant("Féminin");
        ca.setTelRepresentant("+226 76 33 22 11");
        ca.setEmailRepresentant("bureau@aje-bf.org");
        ca.setAdresseRepresentant("Secteur 25, Ouagadougou");
        ca.setNumeroClient("CLI-AS-001");
        ca.setAdresse("Secteur 25, Ouagadougou");
        ca.setEmail("contact@aje-bf.org");
        ca.setTelephone1("+226 76 33 22 11");
        ca.setKycStatus(ClientStatus.VALIDE);
        ca.setKycValidated(true);
        ca.setAgenceRattachee(agence);
        ca = clientAssociationRepository.save(ca);

        CompteBancaire cb3 = new CompteBancaire();
        cb3.setClient(ca);
        cb3.setNumeroCompte("BF00555555555555555555555555");
        cb3.setLibelle("Compte trésorerie");
        cb3.setTypeCompte("CE");
        cb3.setStatut("ACTIF");
        compteBancaireRepository.save(cb3);
    }

    private void loadReleves() {
        List<CompteBancaire> comptes = compteBancaireRepository.findAll();
        List<User> users = userRepository.findAll();
        if (comptes.isEmpty() || users.isEmpty()) return;

        User admin = users.get(0);
        CompteBancaire compte = comptes.get(0);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date debut = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date fin = cal.getTime();

        // Relevé en attente
        ReleveCompte r1 = new ReleveCompte();
        r1.setCompteBancaire(compte);
        r1.setDateSoumission(LocalDateTime.now().minusDays(3));
        r1.setIntervalleDebut(debut);
        r1.setIntervalleFin(fin);
        r1.setStatut("EN_ATTENTE");
        releveCompteRepository.save(r1);

        // Relevé validé
        ReleveCompte r2 = new ReleveCompte();
        r2.setCompteBancaire(compte);
        r2.setDateSoumission(LocalDateTime.now().minusDays(10));
        r2.setDateReception(LocalDateTime.now().minusDays(8));
        r2.setIntervalleDebut(debut);
        r2.setIntervalleFin(fin);
        r2.setStatut("VALIDE");
        r2.setDeliveredBy(admin);
        r2.setFichierReleve("demo/releve-sample.pdf");
        releveCompteRepository.save(r2);

        if (comptes.size() > 1) {
            ReleveCompte r3 = new ReleveCompte();
            r3.setCompteBancaire(comptes.get(1));
            r3.setDateSoumission(LocalDateTime.now().minusDays(1));
            r3.setIntervalleDebut(debut);
            r3.setIntervalleFin(fin);
            r3.setStatut("EN_ATTENTE");
            releveCompteRepository.save(r3);
        }
    }
}
