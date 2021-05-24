package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.models.Forager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerServiceTest {

    ForagerService service = new ForagerService(new ForagerRepositoryDouble());

    @Test
    void shouldAdd() throws DataException {
        Forager forager = new Forager();
        forager.setId("0000-dgwjbjds-213213");
        forager.setFirstName("Tom");
        forager.setLastName("Clency");
        forager.setState("FL");

        Result<Forager> result = service.add(forager);

        assertTrue(result.isSuccess());
        assertEquals(forager, result.getPayload());

    }

    @Test
    void shouldNotAddNull() throws DataException {
        Forager forager = new Forager();
        forager.setId(null);
        forager.setFirstName(null);
        forager.setLastName(null);
        forager.setState(null);

        Result<Forager> result = service.add(forager);
        assertNull(result.getPayload());
        assertEquals(null, forager.getFirstName());
    }

    @Test
    void shouldNotAddDuplicateForager() throws DataException {
        Forager forager = ForagerRepositoryDouble.FORAGER;
        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());

    }
}