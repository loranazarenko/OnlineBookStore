package mate.academy.onlinebookstore.repository.book;

import java.util.List;
import lombok.AllArgsConstructor;
import mate.academy.onlinebookstore.entity.Book;
import mate.academy.onlinebookstore.repository.SpecificationProvider;
import mate.academy.onlinebookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Can't find correct specification provider for key " + key));
    }
}
