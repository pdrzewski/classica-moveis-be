package sptech.classicamoveis.Produto;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProdutoSpecification {

    public static Specification<Produto> filtrar(String nome, Double precoMaximo, Integer categoriaId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"
                ));
            }

            if (precoMaximo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precoVenda"), precoMaximo));
            }

            if (categoriaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
