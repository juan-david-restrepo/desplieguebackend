package com.reporteloya.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.reporteloya.backend.dto.CrearAgenteRequest;
import com.reporteloya.backend.entity.Agentes;
import com.reporteloya.backend.entity.Role;
import com.reporteloya.backend.repository.AgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AgenteService {
    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    public List<Agentes> listarTodos() {
        return agenteRepository.findAll();
    }

    public Page<Agentes> listarTodosPaginado(Pageable pageable) {
        return agenteRepository.findAll(pageable);
    }

    @Transactional
    public Agentes desactivarAgente(Long id) {
        Agentes agente = agenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado"));
        agente.setActivo(false);
        agente.setEstado("FUERA_SERVICIO");
        return agenteRepository.save(agente);
    }

    @Transactional
    public Agentes activarAgente(Long id) {
        Agentes agente = agenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado"));
        agente.setActivo(true);
        agente.setEstado("DISPONIBLE");
        return agenteRepository.save(agente);
    }

    public Optional<Agentes> buscarPorPlaca(String placa) {
        return agenteRepository.findByPlacaIgnoreCase(placa);
    }

    public Agentes guardar(Agentes agente) {
        return agenteRepository.saveAndFlush(agente);
    }

    public Optional<Agentes> buscarPorEmail(String email) {
        return agenteRepository.findByEmail(email);
    }

    @Transactional
    public Agentes crearAgente(CrearAgenteRequest request) {
        validarCreacion(request);

        if (agenteRepository.existsByEmail(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        if (request.getPlaca() != null && !request.getPlaca().isBlank()
                && agenteRepository.findByPlacaIgnoreCase(request.getPlaca()).isPresent()) {
            throw new IllegalArgumentException("La placa ya está registrada.");
        }

        String docAgente = request.getDocumento() != null && !request.getDocumento().isBlank()
                ? request.getDocumento()
                : request.getNumeroDocumento();

        if (docAgente != null && agenteRepository.existsByDocumento(docAgente)) {
            throw new IllegalArgumentException("El documento ya está registrado.");
        }

        Agentes agente = new Agentes();

        // Campos heredados de Usuario
        agente.setEmail(request.getCorreo());
        agente.setPassword(passwordEncoder.encode(request.getPassword()));
        agente.setNombreCompleto(request.getNombreCompleto());
        agente.setTipoDocumento(request.getTipoDocumento());
        agente.setNumeroDocumento(request.getNumeroDocumento());
        agente.setRole(Role.AGENTE);
        agente.setEmailVerificado(true);

        // Campos propios de Agentes
        agente.setNombre(request.getNombre() != null && !request.getNombre().isBlank()
                ? request.getNombre()
                : request.getNombreCompleto());
        agente.setPlaca(request.getPlaca());
        agente.setTelefono(request.getTelefono());
        agente.setDocumento(docAgente);
        agente.setEstado(request.getEstado() != null && !request.getEstado().isBlank()
                ? request.getEstado().toUpperCase()
                : "DISPONIBLE");
        agente.setFoto(request.getFoto());
        agente.setResumenProfesional1(request.getResumenProfesional1());
        agente.setResumenProfesional2(request.getResumenProfesional2());
        agente.setResumenProfesional3(request.getResumenProfesional3());
        agente.setResumenProfesional4(request.getResumenProfesional4());

        return agenteRepository.save(agente);
    }

    private void validarCreacion(CrearAgenteRequest request) {
        if (request.getCorreo() == null || request.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo es requerido.");
        }
        if (!request.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("El formato del correo no es válido.");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es requerida.");
        }
        if (!isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&).");
        }
        if (request.getNombreCompleto() == null || request.getNombreCompleto().isBlank()) {
            throw new IllegalArgumentException("El nombre completo es requerido.");
        }
        if (request.getPlaca() == null || request.getPlaca().isBlank()) {
            throw new IllegalArgumentException("La placa es requerida.");
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("@$!%*?&".indexOf(c) >= 0) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public String subirFotoACloudinary(byte[] imageBytes, String fileName) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                imageBytes,
                ObjectUtils.asMap(
                    "public_id", "perfiles/" + fileName,
                    "overwrite", true,
                    "resource_type", "image"
                )
            );
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen a Cloudinary", e);
        }
    }
}