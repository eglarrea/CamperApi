package hemen.go.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import hemen.go.entity.Usuario;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 *
 * Esta interfaz proporciona acceso a operaciones CRUD y consultas personalizadas
 * sobre la tabla de usuarios. Extiende {@link JpaRepository} para operaciones básicas
 * como guardar, eliminar y buscar por ID.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Gestionar la persistencia de usuarios en la base de datos.</li>
 *   <li>Permitir consultas específicas relacionadas con el email y la empresa asociada.</li>
 *   <li>Facilitar validaciones de existencia y búsquedas por atributos clave.</li>
 * </ul>
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Verifica si existe un usuario con el email indicado.
     *
     * <p>Uso típico:</p>
     * <ul>
     *   <li>Validar unicidad de email en procesos de registro.</li>
     * </ul>
     *
     * @param emailPersona correo electrónico del usuario.
     * @return {@code true} si existe un usuario con ese email, {@code false} en caso contrario.
     */
   //boolean existsByEmailPersona(String emailPersona);

    /**
     * Busca un usuario por su email.
     *
     * <p>Uso típico:</p>
     * <ul>
     *   <li>Autenticación y carga de datos de usuario.</li>
     *   <li>Validar existencia de un usuario en procesos de negocio.</li>
     * </ul>
     *
     * @param email correo electrónico del usuario.
     * @return un {@link Optional} que contiene el usuario si existe.
     */
    Optional<Usuario> findByEmailPersona(String email);

    /**
     * Obtiene todos los usuarios asociados a una empresa concreta.
     *
     * <p>Uso típico:</p>
     * <ul>
     *   <li>Listar empleados o usuarios vinculados a una empresa.</li>
     * </ul>
     *
     * @param idEmpresa identificador de la empresa.
     * @return lista de usuarios pertenecientes a la empresa.
     */
    List<Usuario> findByEmpresa_Id(Long idEmpresa);
}
