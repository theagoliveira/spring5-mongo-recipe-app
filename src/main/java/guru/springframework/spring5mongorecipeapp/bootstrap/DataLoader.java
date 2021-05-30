package guru.springframework.spring5mongorecipeapp.bootstrap;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import guru.springframework.spring5mongorecipeapp.domain.Category;
import guru.springframework.spring5mongorecipeapp.domain.Difficulty;
import guru.springframework.spring5mongorecipeapp.domain.Ingredient;
import guru.springframework.spring5mongorecipeapp.domain.Notes;
import guru.springframework.spring5mongorecipeapp.domain.Recipe;
import guru.springframework.spring5mongorecipeapp.domain.UnitOfMeasure;
import guru.springframework.spring5mongorecipeapp.repositories.CategoryRepository;
import guru.springframework.spring5mongorecipeapp.repositories.RecipeRepository;
import guru.springframework.spring5mongorecipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryRepository categoryRepository;

    public DataLoader(RecipeRepository recipeRepository,
                      UnitOfMeasureRepository unitOfMeasureRepository,
                      CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0L) {
            log.debug("Loading categories...");
            loadCategories();
        } else {
            log.debug("Categories already loaded.");
        }

        if (unitOfMeasureRepository.count() == 0L) {
            log.debug("Loading units of measure...");
            loadUnitsOfMeasure();
        } else {
            log.debug("Units of measure already loaded.");
        }

        if (recipeRepository.count() == 0L) {
            log.debug("Loading recipes...");
            loadRecipes();
        } else {
            log.debug("Recipes already loaded.");
        }
    }

    private void loadCategories() {
        var cat1 = new Category();
        cat1.setDescription("american");
        categoryRepository.save(cat1);

        var cat2 = new Category();
        cat2.setDescription("italian");
        categoryRepository.save(cat2);

        var cat3 = new Category();
        cat3.setDescription("mexican");
        categoryRepository.save(cat3);

        var cat4 = new Category();
        cat4.setDescription("fast food");
        categoryRepository.save(cat4);
    }

    private void loadUnitsOfMeasure() {
        var uom1 = new UnitOfMeasure();
        uom1.setDescription("teaspoon");
        unitOfMeasureRepository.save(uom1);

        var uom2 = new UnitOfMeasure();
        uom2.setDescription("tablespoon");
        unitOfMeasureRepository.save(uom2);

        var uom3 = new UnitOfMeasure();
        uom3.setDescription("cup");
        unitOfMeasureRepository.save(uom3);

        var uom4 = new UnitOfMeasure();
        uom4.setDescription("pinch");
        unitOfMeasureRepository.save(uom4);

        var uom5 = new UnitOfMeasure();
        uom5.setDescription("ounce");
        unitOfMeasureRepository.save(uom5);

        var uom6 = new UnitOfMeasure();
        uom6.setDescription("pint");
        unitOfMeasureRepository.save(uom6);

        var uom7 = new UnitOfMeasure();
        uom7.setDescription("dash");
        unitOfMeasureRepository.save(uom7);

        var uom8 = new UnitOfMeasure();
        uom8.setDescription("blank");
        unitOfMeasureRepository.save(uom8);
    }

    private void loadRecipes() {
        log.debug("Getting UnitOfMeasure and Category objects.");

        var tspUom = unitOfMeasureRepository.findByDescription("teaspoon").get();
        var tbspUom = unitOfMeasureRepository.findByDescription("tablespoon").get();
        var dashUom = unitOfMeasureRepository.findByDescription("dash").get();
        var blankUom = unitOfMeasureRepository.findByDescription("blank").get();

        var mexicanCategory = categoryRepository.findByDescription("mexican").get();
        var americanCategory = categoryRepository.findByDescription("american").get();

        log.debug("Creating Ingredient objects.");

        var ripeAvocados = new Ingredient();
        ripeAvocados.setAmount(BigDecimal.valueOf(2));
        ripeAvocados.setDescription("ripe avocados");
        ripeAvocados.setUom(blankUom);

        var salt = new Ingredient();
        salt.setAmount(BigDecimal.valueOf(0.25));
        salt.setDescription("salt, more to taste");
        salt.setUom(tspUom);

        var freshLimeJuice = new Ingredient();
        freshLimeJuice.setAmount(BigDecimal.valueOf(1));
        freshLimeJuice.setDescription("fresh lime juice or lemon juice");
        freshLimeJuice.setUom(tbspUom);

        var mincedRedOnion = new Ingredient();
        mincedRedOnion.setAmount(BigDecimal.valueOf(1));
        mincedRedOnion.setDescription("minced red onion or thinly sliced green onion");
        mincedRedOnion.setUom(tbspUom);

        var serranoChiles = new Ingredient();
        serranoChiles.setAmount(BigDecimal.valueOf(2));
        serranoChiles.setDescription("serrano chiles, stems and seeds removed, minced");
        serranoChiles.setUom(blankUom);

        var cilantro = new Ingredient();
        cilantro.setAmount(BigDecimal.valueOf(2));
        cilantro.setDescription("cilantro (leaves and tender stems), finely chopped");
        cilantro.setUom(tbspUom);

        var freshlyGratedBlackPepper = new Ingredient();
        freshlyGratedBlackPepper.setAmount(BigDecimal.valueOf(1.0));
        freshlyGratedBlackPepper.setDescription("freshly grated black pepper");
        freshlyGratedBlackPepper.setUom(dashUom);

        var ripeTomato = new Ingredient();
        ripeTomato.setAmount(BigDecimal.valueOf(0.5));
        ripeTomato.setDescription("ripe tomato, seeds and pulp removed, chopped");
        ripeTomato.setUom(blankUom);

        var redRadishes = new Ingredient();
        redRadishes.setAmount(null);
        redRadishes.setDescription("red radishes or jicama, to garnish");
        redRadishes.setUom(blankUom);

        var tortillaChips = new Ingredient();
        tortillaChips.setAmount(null);
        tortillaChips.setDescription("tortilla chips, to serve");
        tortillaChips.setUom(blankUom);

        var ripeAvocadosCopy = new Ingredient();
        ripeAvocadosCopy.setAmount(BigDecimal.valueOf(2));
        ripeAvocadosCopy.setDescription("ripe avocados");
        ripeAvocadosCopy.setUom(blankUom);

        var saltCopy = new Ingredient();
        saltCopy.setAmount(BigDecimal.valueOf(0.25));
        saltCopy.setDescription("salt, more to taste");
        saltCopy.setUom(tspUom);

        var freshLimeJuiceCopy = new Ingredient();
        freshLimeJuiceCopy.setAmount(BigDecimal.valueOf(1));
        freshLimeJuiceCopy.setDescription("fresh lime juice or lemon juice");
        freshLimeJuiceCopy.setUom(tbspUom);

        var mincedRedOnionCopy = new Ingredient();
        mincedRedOnionCopy.setAmount(BigDecimal.valueOf(1));
        mincedRedOnionCopy.setDescription("minced red onion or thinly sliced green onion");
        mincedRedOnionCopy.setUom(tbspUom);

        var serranoChilesCopy = new Ingredient();
        serranoChilesCopy.setAmount(BigDecimal.valueOf(2));
        serranoChilesCopy.setDescription("serrano chiles, stems and seeds removed, minced");
        serranoChilesCopy.setUom(blankUom);

        var cilantroCopy = new Ingredient();
        cilantroCopy.setAmount(BigDecimal.valueOf(2));
        cilantroCopy.setDescription("cilantro (leaves and tender stems), finely chopped");
        cilantroCopy.setUom(tbspUom);

        var freshlyGratedBlackPepperCopy = new Ingredient();
        freshlyGratedBlackPepperCopy.setAmount(BigDecimal.valueOf(1.0));
        freshlyGratedBlackPepperCopy.setDescription("freshly grated black pepper");
        freshlyGratedBlackPepperCopy.setUom(dashUom);

        var ripeTomatoCopy = new Ingredient();
        ripeTomatoCopy.setAmount(BigDecimal.valueOf(0.5));
        ripeTomatoCopy.setDescription("ripe tomato, seeds and pulp removed, chopped");
        ripeTomatoCopy.setUom(blankUom);

        var redRadishesCopy = new Ingredient();
        redRadishesCopy.setAmount(null);
        redRadishesCopy.setDescription("red radishes or jicama, to garnish");
        redRadishesCopy.setUom(blankUom);

        var tortillaChipsCopy = new Ingredient();
        tortillaChipsCopy.setAmount(null);
        tortillaChipsCopy.setDescription("tortilla chips, to serve");
        tortillaChipsCopy.setUom(blankUom);

        log.debug("Creating Notes objects.");

        var avocadoNotes = new Notes();
        avocadoNotes.setRecipeNotes(
            "The trick to making perfect guacamole is using ripe avocados that are just the right"
                    + " amount of ripeness. Not ripe enough and the avocado will be hard and tasteless."
                    + " Too ripe and the taste will be off."
        );

        var avocadoNotesCopy = new Notes();
        avocadoNotesCopy.setRecipeNotes(
            "The trick to making perfect guacamole is using ripe avocados that are just the right"
                    + " amount of ripeness. Not ripe enough and the avocado will be hard and tasteless."
                    + " Too ripe and the taste will be off."
        );

        log.debug("Creating Recipe objects.");

        var perfectGuacamole = new Recipe();
        perfectGuacamole.setName("Perfect Guacamole");
        perfectGuacamole.setDescription(
            "The best guacamole keeps it simple: just ripe avocados, salt, a squeeze of lime, "
                    + "onions, chiles, cilantro, and some chopped tomato. Serve it as a dip at your "
                    + "next party or spoon it on top of tacos for an easy dinner upgrade."
        );
        perfectGuacamole.setPrepTime(10);
        perfectGuacamole.setCookTime(0);
        perfectGuacamole.setServings(4);
        perfectGuacamole.setSource("Simply Recipes");
        perfectGuacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        perfectGuacamole.setDirections(
            "<li>Prepare a gas or charcoal grill for medium-high, direct heat</li>"
                    + "<li>Make the marinade and coat the chicken:<p>In a large bowl, stir together "
                    + "the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. "
                    + "Stir in the orange juice and olive oil to make a loose paste. Add the "
                    + "chicken to the bowl and toss to coat all over.</p>"
                    + "<p>Set aside to marinate while the grill heats and you prepare the rest of "
                    + "the toppings.</p>" + "</li>"
                    + "<li>Grill the chicken:<p>Grill the chicken for 3 to 4 minutes per side, or "
                    + "until a thermometer inserted into the thickest part of the meat registers "
                    + "165F. Transfer to a plate and rest for 5 minutes.</p>" + "</li>"
                    + "<li>Warm the tortillas:<p>Place each tortilla on the grill or on a hot, dry "
                    + "skillet over medium-high heat. As soon as you see pockets of the air "
                    + "start to puff up in the tortilla, turn it with tongs and heat for a few "
                    + "seconds on the other side.</p>"
                    + "<p>Wrap warmed tortillas in a tea towel to keep them warm until serving."
                    + "</p>" + "</li>"
                    + "<li>Assemble the tacos:<p>Slice the chicken into strips. On each tortilla, "
                    + "place a small handful of arugula. Top with chicken slices, sliced "
                    + "avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned "
                    + "sour cream. Serve with lime wedges.</p>" + "</li>"
        );
        perfectGuacamole.setDifficulty(Difficulty.EASY);
        perfectGuacamole.setNotes(avocadoNotes);
        perfectGuacamole.addIngredient(ripeAvocados);
        perfectGuacamole.addIngredient(salt);
        perfectGuacamole.addIngredient(freshLimeJuice);
        perfectGuacamole.addIngredient(mincedRedOnion);
        perfectGuacamole.addIngredient(serranoChiles);
        perfectGuacamole.addIngredient(cilantro);
        perfectGuacamole.addIngredient(freshlyGratedBlackPepper);
        perfectGuacamole.addIngredient(ripeTomato);
        perfectGuacamole.addIngredient(redRadishes);
        perfectGuacamole.addIngredient(tortillaChips);
        perfectGuacamole.getCategories().add(mexicanCategory);
        perfectGuacamole.getCategories().add(americanCategory);

        var perfectGuacamoleCopy = new Recipe();
        perfectGuacamoleCopy.setName("Perfect Guacamole Copy");
        perfectGuacamoleCopy.setDescription(
            "The best guacamole keeps it simple: just ripe avocados, salt, a squeeze of lime, "
                    + "onions, chiles, cilantro, and some chopped tomato. Serve it as a dip at your "
                    + "next party or spoon it on top of tacos for an easy dinner upgrade."
        );
        perfectGuacamoleCopy.setPrepTime(10);
        perfectGuacamoleCopy.setCookTime(0);
        perfectGuacamoleCopy.setServings(4);
        perfectGuacamoleCopy.setSource("Simply Recipes");
        perfectGuacamoleCopy.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        perfectGuacamoleCopy.setDirections(
            "<li>Prepare a gas or charcoal grill for medium-high, direct heat</li>"
                    + "<li>Make the marinade and coat the chicken:<p>In a large bowl, stir together "
                    + "the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. "
                    + "Stir in the orange juice and olive oil to make a loose paste. Add the "
                    + "chicken to the bowl and toss to coat all over.</p>"
                    + "<p>Set aside to marinate while the grill heats and you prepare the rest of "
                    + "the toppings.</p>" + "</li>"
                    + "<li>Grill the chicken:<p>Grill the chicken for 3 to 4 minutes per side, or "
                    + "until a thermometer inserted into the thickest part of the meat registers "
                    + "165F. Transfer to a plate and rest for 5 minutes.</p>" + "</li>"
                    + "<li>Warm the tortillas:<p>Place each tortilla on the grill or on a hot, dry "
                    + "skillet over medium-high heat. As soon as you see pockets of the air "
                    + "start to puff up in the tortilla, turn it with tongs and heat for a few "
                    + "seconds on the other side.</p>"
                    + "<p>Wrap warmed tortillas in a tea towel to keep them warm until serving."
                    + "</p>" + "</li>"
                    + "<li>Assemble the tacos:<p>Slice the chicken into strips. On each tortilla, "
                    + "place a small handful of arugula. Top with chicken slices, sliced "
                    + "avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned "
                    + "sour cream. Serve with lime wedges.</p>" + "</li>"
        );
        perfectGuacamoleCopy.setDifficulty(Difficulty.EASY);
        perfectGuacamoleCopy.setNotes(avocadoNotesCopy);
        perfectGuacamoleCopy.addIngredient(ripeAvocadosCopy);
        perfectGuacamoleCopy.addIngredient(saltCopy);
        perfectGuacamoleCopy.addIngredient(freshLimeJuiceCopy);
        perfectGuacamoleCopy.addIngredient(mincedRedOnionCopy);
        perfectGuacamoleCopy.addIngredient(serranoChilesCopy);
        perfectGuacamoleCopy.addIngredient(cilantroCopy);
        perfectGuacamoleCopy.addIngredient(freshlyGratedBlackPepperCopy);
        perfectGuacamoleCopy.addIngredient(ripeTomatoCopy);
        perfectGuacamoleCopy.addIngredient(redRadishesCopy);
        perfectGuacamoleCopy.addIngredient(tortillaChipsCopy);
        perfectGuacamoleCopy.getCategories().add(mexicanCategory);
        perfectGuacamoleCopy.getCategories().add(americanCategory);

        log.debug("Saving recipes.");

        recipeRepository.save(perfectGuacamole);
        recipeRepository.save(perfectGuacamoleCopy);
    }

}
