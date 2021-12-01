package Libreria.repositorios;

import Libreria.entidades.Libro;
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
public interface LibroRepositorio extends JpaRepository<Libro, String> {
    
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarLibroporTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT l FROM Libro l ORDER BY l.titulo")
    public List<Libro> listarLibrosOrdenadosNombre();
    
    @Query("SELECT l FROM Libro l ORDER BY l.autor.nombre")
    public List<Libro> listarLibrosOrdenadosautor();
    
    @Query("SELECT p FROM Prestamo p WHERE p.libro.id = :idlibro")
    public List<Prestamo> validarLibroPrestamo(@Param("idlibro") String idlibro);
}
