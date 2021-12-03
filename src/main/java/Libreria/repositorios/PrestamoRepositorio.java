package Libreria.repositorios;

import Libreria.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Xavier Pocchettino
 */
@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {
    
    @Query("SELECT p FROM Prestamo p ORDER BY p.usuario.nombre")
    public List<Prestamo> buscarPrestamosPorUsuarios();
    
    @Query("SELECT p FROM Prestamo p ORDER BY p.libro.titulo")
    public List<Prestamo> buscarPrestamosPorTitulo();
    
    @Query("SELECT p FROM Prestamo p WHERE p.usuario.id = :idusuario")
    public List<Prestamo> buscarPorIDUsuario(@Param("idusuario") String idusuario);
}
