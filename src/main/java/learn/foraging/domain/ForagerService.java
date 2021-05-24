package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepository;
import learn.foraging.models.Forager;

import java.util.List;
import java.util.stream.Collectors;

public class ForagerService {

    private final ForagerRepository repository;

    public ForagerService(ForagerRepository repository) {
        this.repository = repository;
    }

    public List<Forager> findByState(String stateAbbr) {
        return repository.findByState(stateAbbr);
    }

    public List<Forager> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public Result<Forager> add(Forager forager) throws DataException {
        Result<Forager> result = validate(forager);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(forager));

        return result;
    }

    private Result<Forager> validate(Forager forager) {

        Result<Forager> result = validateNulls(forager);
        if (!result.isSuccess()) {
            return result;
        }

        validateDuplicate(forager, result);
        return result;
    }

    private Result<Forager> validateNulls(Forager forager) {
        Result<Forager> result = new Result<>();

        if (forager == null) {
            result.addErrorMessage("Nothing to save");
            return result;
        }

        if (forager.getFirstName() == null && forager.getLastName() == null) {
            result.addErrorMessage("Name is required");
        }

        if (forager.getState() == null) {
            result.addErrorMessage("State is required");
        }

        return result;
    }

    private Result<Forager> validateInputs(Forager forager) {
        Result<Forager> result = new Result<>();

        if (!forager.getFirstName().matches("[a-zA-Z_]+")) {
            result.addErrorMessage("Invalid Name");
            return result;
        }

        if (!forager.getLastName().matches("[a-zA-Z_]+")) {
            result.addErrorMessage("Invalid Name");
            return result;
        }

//        if (!forager.getState().matches("[a-zA-Z_]+") && forager.getState().chars(2)) {
//            result.addErrorMessage("Invalid Name");
//            return result;
//        }
        return result;
    }

    private void validateDuplicate(Forager forager, Result<Forager> result) {
        List<Forager> foragers = repository.findAll();
        for (Forager f : foragers) {
            if (forager.getFirstName().equals(f.getFirstName())) {
                if (forager.getLastName().equals(f.getLastName())) {
                    if (forager.getState().equals(f.getState())) {
                        result.addErrorMessage("Duplicate Forager is not allowed");
                    }
                }
            }
        }
    }
}

