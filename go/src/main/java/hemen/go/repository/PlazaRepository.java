package hemen.go.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import hemen.go.entity.Plaza;

/**
 * Repositorio JPA para la entidad {@link Plaza}.
 *
 * Esta interfaz proporciona acceso a operaciones CRUD y consultas personalizadas
 * sobre la tabla de plazas de parking. Extiende:
 * <ul>
 *   <li>{@link JpaRepository} para operaciones básicas (guardar, eliminar, buscar por ID, etc.).</li>
 *   <li>{@link JpaSpecificationExecutor} para consultas dinámicas mediante Specifications.</li>
 * </ul>
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Gestionar la persistencia de plazas en la base de datos.</li>
 *   <li>Permitir consultas específicas relacionadas con empresas y parkings.</li>
 *   <li>Facilitar búsquedas dinámicas con filtros complejos.</li>
 * </ul>
 */
public interface PlazaRepository extends JpaRepository<Plaza, Long> , JpaSpecificationExecutor<Plaza>{
	
	/*@Query("SELECT p FROM Plaza p LEFT JOIN Reserva r "
			+ "  ON p.id = r.plaza.id "
			+ "  AND r.fecInicio < :fechaHasta "
			+ "  AND r.fecFin > :fechaDesde "
			+ "  AND r.estado = '1' WHERE r.id IS NULL")
	List<Plaza> findAllByPlazasLibres(@Param("fechaDesde") Date fechaDesde, @Param("fechaHasta") Date fechaHasta);*/
	
	/**
     * Busca una plaza concreta por su ID y el ID de la empresa asociada al parking.
     *
     * <p>Uso típico:</p>
     * <ul>
     *   <li>Validar que una plaza pertenece a una empresa concreta.</li>
     *   <li>Restringir acceso a plazas según el contexto de la empresa.</li>
     * </ul>
     *
     * @param idPlaza identificador de la plaza.
     * @param idEmpresa identificador de la empresa propietaria del parking.
     * @return un {@link Optional} que contiene la plaza si existe y pertenece a la empresa.
     */
	Optional<Plaza> findByIdAndParking_Empresa_Id(Long idPlaza, Long idEmpresa);

}
