package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageRepositoryDouble;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.data.ItemRepositoryDouble;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.Test;

import javax.naming.Name;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ForageServiceTest {

    ForageService service = new ForageService(
            new ForageRepositoryDouble(),
            new ForagerRepositoryDouble(),
            new ItemRepositoryDouble());

    @Test
    void shouldAdd() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(36, result.getPayload().getId().length());
    }

    @Test
    void shouldNotAddWhenForagerNotFound() throws DataException {
        Forager forager = new Forager();
        forager.setId("30816379-188d-4552-913f-9a48405e8c08");
        forager.setFirstName("Ermengarde");
        forager.setLastName("Sansom");
        forager.setState("NM");

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(forager);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenItemNotFound() throws DataException {
        Item item = new Item(11, "Dandelion", Category.EDIBLE, new BigDecimal("0.05"));

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(item);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddDuplicate() throws DataException {
        Forage forage = new Forage();
        forage.setId("a86a5af2-27dd-4313-9bb9-cb6f56282ce2,90f110bf-1242-4945");
        forage.setDate(LocalDate.of(2020, 6, 26));
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(1.25);
        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddFutureDate() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.of(2221, 10, 10));
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(50);
        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithTooLargeKg() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.of(2012, 12, 03));
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(2300);
        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithNegativeKg() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.of(2012, 12, 03));
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(-1);
        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

}