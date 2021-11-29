package Libreria.repositorios;

import Libreria.entidades.Autor;
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
public interface AutorRepositorio extends JpaRepository<Autor, String> {
    
    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre ORDER BY a.nombre")
    public Autor buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT a FROM Autor a ORDER BY a.nombre")
    public List<Autor> buscarAutoresOrdenados();
    
    @Query("SELECT l FROM Libro l WHERE l.autor.id = :idautor")
    public List<Libro> validarAutorLibro(@Param("idautor") String idautor);
    
}
