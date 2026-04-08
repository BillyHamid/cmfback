package com.adouk.finacombackend.domain.beans;

import com.adouk.finacombackend.Application.services.Interfaces.UserService;
import com.adouk.finacombackend.domain.aggregates.Permission;
import com.adouk.finacombackend.domain.aggregates.Role;
import com.adouk.finacombackend.domain.aggregates.RolePermission;
import com.adouk.finacombackend.domain.commands.UserCommand;
import com.adouk.finacombackend.domain.aggregates.Agence;
import com.adouk.finacombackend.infrastructure.repositories.AgenceRepository;
import com.adouk.finacombackend.infrastructure.repositories.PermissionRepository;
import com.adouk.finacombackend.infrastructure.repositories.RolePermissionRepository;
import com.adouk.finacombackend.infrastructure.repositories.RoleRepository;
import com.adouk.finacombackend.infrastructure.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BootstrapService implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserService userService;
    private final AgenceRepository agenceRepository;
    private final Environment environment;

    @Autowired
    public BootstrapService(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PermissionRepository permissionRepository,
                            UserService userService, RolePermissionRepository rolePermissionRepository,
                            AgenceRepository agenceRepository,
                            Environment environment
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userService = userService;
        this.agenceRepository = agenceRepository;
        this.environment = environment;
    }

    private boolean isDemoProfile() {
        for (String profile : environment.getActiveProfiles()) {
            if ("demo".equals(profile)) return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadSecurityData();
    }

    @Transactional
    private void loadSecurityData() {
        // MODULE PERMISSIONS
        createPermissionIfNotExist("view_user", "Voir les utilisateurs");
        createPermissionIfNotExist("create_user", "Créer un utilisateur");
        createPermissionIfNotExist( "update_user", "Modifier un utilisateur");
        createPermissionIfNotExist( "delete_user", "Supprimer un utilisateur");
        createPermissionIfNotExist( "view_role", "Voir les rôles");
        createPermissionIfNotExist( "create_role", "Créer un rôle");
        createPermissionIfNotExist( "update_role", "Modifier un rôle");
        createPermissionIfNotExist( "delete_role", "Supprimer un rôle");

        createPermissionIfNotExist( "view_agence", "Voir les agences");
        createPermissionIfNotExist( "create_agence", "Créer une agence");
        createPermissionIfNotExist( "update_agence", "Modifier une agence");
        createPermissionIfNotExist( "delete_agence", "Supprimer une agence");

        createPermissionIfNotExist( "view_client", "Voir les client");
        createPermissionIfNotExist( "create_client", "Créer un client");
        createPermissionIfNotExist( "update_client", "Modifier un client");
        createPermissionIfNotExist( "delete_client", "Supprimer un client");

        createPermissionIfNotExist( "view_client_dossier", "Voir les dossiers clients");
        createPermissionIfNotExist( "validate_dossier", "Valider un dossier client");

        createPermissionIfNotExist( "view_client_releve", "Voir les relevés clients");
        createPermissionIfNotExist( "submit_releve", "Soumettre un relevé client");

        createPermissionIfNotExist( "view_faq", "Voir les FAQ");
        createPermissionIfNotExist( "create_faq", "Créer une FAQ");
        createPermissionIfNotExist( "update_faq", "Modifier une FAQ");
        createPermissionIfNotExist( "delete_faq", "Supprimer une FAQ");

        createPermissionIfNotExist( "view_piece_justificative", "Voir les pièces de justificatif");
        createPermissionIfNotExist( "create_piece_justificative", "Créer une pièce de justificatif");
        createPermissionIfNotExist( "update_piece_justificative", "Modifier une pièce de justificatif");
        createPermissionIfNotExist( "delete_piece_justificative", "Supprimer une pièce de justificatif");

        createPermissionIfNotExist( "view_service", "Voir les services");
        createPermissionIfNotExist( "create_service", "Créer un service");
        createPermissionIfNotExist( "update_service", "Modifier un service");
        createPermissionIfNotExist( "update_agence", "Modifier une agence");
        createPermissionIfNotExist( "delete_agence", "Supprimer une agence");

        createPermissionIfNotExist( "view_notification", "Voir les notifications");
        createPermissionIfNotExist( "create_notification", "Créer une notification");
        createPermissionIfNotExist( "update_notification", "Modifier une notification");
        createPermissionIfNotExist( "delete_notification", "Supprimer une notification");

        createPermissionIfNotExist( "view_user_session", "Voir les sessions utilisateurs");
        createPermissionIfNotExist( "delete_agence", "Supprimer une agence");
        createPermissionIfNotExist( "delete_agence", "Supprimer une agence");

        createPermissionIfNotExist( "view_dashboard", "Voir le tableau de bord");

        // Permissions agent commercial / prospection
        createPermissionIfNotExist("view_my_prospects", "Voir mes prospects");
        createPermissionIfNotExist("create_prospect", "Créer un prospect");
        createPermissionIfNotExist("update_prospect", "Modifier un prospect");
        createPermissionIfNotExist("view_commercial_dashboard", "Voir le tableau de bord commercial");

        createUsersIfNotExists();
        createCommercialRoleIfNotExists();
        if (!isDemoProfile()) {
            createDefaultAgenceIfNotExists();
            createCommercialUserIfNotExists();
        }
    }

    private void createDefaultAgenceIfNotExists() {
        if (!agenceRepository.findAll().isEmpty()) return;
        Agence agence = new Agence();
        agence.setAgencyName("Agence principale");
        agence.setAgencyAdress("Siège");
        agence.setAgencyPhone1("+226 00 00 00 00");
        agence.setActive(true);
        agenceRepository.save(agence);
    }

    private void createCommercialUserIfNotExists() {
        if (userRepository.findByUsername("commercial").isPresent()) return;
        if (roleRepository.findByCode("agent-commercial") == null) return;
        List<Agence> agences = agenceRepository.findAll();
        if (agences.isEmpty()) return;
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

    private void createPermissionIfNotExist(String perm, String name) {
        if (permissionRepository.findByCode(perm) == null) {
            Permission permission = new Permission();
            permission.setCode(perm);
            permission.setLabel(name);
            permissionRepository.save(permission);
        }
    }

    private void createUsersIfNotExists() {
        if (roleRepository.findByCode("super-administrateur") != null) {
            return;
        }
        Role role = new Role();
        role.setLabel("Super administrateur");
        role.setCode("super-administrateur");
        role = roleRepository.saveAndFlush(role);
        List<Permission> permissions = permissionRepository.findAll();
        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            System.out.println("create permission " + permission.getCode() + " for role " + role.getCode());
            rolePermissionRepository.save(rolePermission);
        }
        UserCommand user = new UserCommand();
        user.setFirstname("admin");
        user.setLastname("admin");
        user.setPhone("+2125550101");
        user.setUsername("admin");
        user.setEmail("ekouda006@gmail.com");
        user.setRole("super-administrateur");
        userService.createAgent(user);
    }

    private void createCommercialRoleIfNotExists() {
        if (roleRepository.findByCode("agent-commercial") != null) {
            return;
        }
        Role role = new Role();
        role.setLabel("Agent commercial");
        role.setCode("agent-commercial");
        role = roleRepository.saveAndFlush(role);
        String[] commercialPerms = {
            "view_my_prospects", "create_prospect", "update_prospect", "view_commercial_dashboard",
            "view_dashboard", "view_client_dossier", "view_client", "view_faq", "view_service",
            "view_piece_justificative"
        };
        for (String code : commercialPerms) {
            Permission perm = permissionRepository.findByCode(code);
            if (perm != null) {
                RolePermission rp = new RolePermission();
                rp.setRole(role);
                rp.setPermission(perm);
                rolePermissionRepository.save(rp);
            }
        }
    }

}
