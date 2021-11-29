package Libreria.repositorios;

import Libreria.entidades.Editorial;
import Libreria.entidades.Libro;
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
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {
    
    @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public Editorial buscarEditorialporNombre(@Param("nombre")String nombre);
    
    @Query("SELECT e FROM Editorial e ORDER BY e.nombre")
    public List<Editorial> buscarEditorialOrdenada();
    
    @Query("SELECT l FROM Libro l WHERE l.editorial.id = :ideditorial")
    public List<Libro> validarEditorialLibro(@Param("ideditorial") String ideditorial);
    
}
