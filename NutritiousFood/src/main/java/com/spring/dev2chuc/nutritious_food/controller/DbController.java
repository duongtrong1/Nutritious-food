package com.spring.dev2chuc.nutritious_food.controller;

import com.spring.dev2chuc.nutritious_food.model.*;
import com.spring.dev2chuc.nutritious_food.payload.response.ApiResponseError;
import com.spring.dev2chuc.nutritious_food.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/db")
public class DbController {

    private static final Logger LOGGER = Logger.getLogger(DbController.class.getSimpleName());

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    ComboRepository comboRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/{name}")
    public ResponseEntity<?> authenticateUser(@PathVariable("name") String name) {
        try {
            String fileName = "src/main/java/com/spring/dev2chuc/nutritious_food/config/Seeding.java";
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.print(getStartOfFile() +
                    getFunctionSeedingRole() +
                    getFunctionSeedingUser() +
                    getFunctionSeedingUserProfile() +
                    getFunctionSeedingAddress() +
                    getFunctionSeedingCategory() +
                    getFunctionSeedingFood() +
                    getFunctionSeedingCombo() +
                    getFunctionSeedingSchedule() +
                    getEndOfFile());
            printWriter.close();
            System.out.println("Done!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(
                new ApiResponseError(HttpStatus.OK.value(),
                        "Write success"),
                HttpStatus.OK);
    }

    private String getStartOfFile() {
        return "package com.spring.dev2chuc.nutritious_food.config;\n" +
                "\n" +
                "import com.spring.dev2chuc.nutritious_food.model.*;\n" +
                "import com.spring.dev2chuc.nutritious_food.repository.*;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.context.event.ApplicationReadyEvent;\n" +
                "import org.springframework.context.ApplicationListener;\n" +
                "import org.springframework.security.crypto.password.PasswordEncoder;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "\n" +
                "import java.util.*;\n" +
                "import java.util.logging.Level;\n" +
                "import java.util.logging.Logger;\n" +
                "\n" +
                "@Component\n" +
                "public class Seeding implements ApplicationListener<ApplicationReadyEvent> {\n" +
                "\n" +
                "    private static final Logger LOGGER = Logger.getLogger(Seeding.class.getSimpleName());\n" +
                "\n" +
                "    @Autowired\n" +
                "    PasswordEncoder passwordEncoder;\n" +
                "\n" +
                "    @Autowired\n" +
                "    RoleRepository roleRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    UserRepository userRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    HistoryRepository historyRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    AddressRepository addressRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    UserProfileRepository userProfileRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    CategoryRepository categoryRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    FoodRepository foodRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    ComboRepository comboRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    ScheduleRepository scheduleRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    OrderDetailRepository orderDetailRepository;\n" +
                "\n" +
                "    @Autowired\n" +
                "    OrderRepository orderRepository;\n" +
                "\n" +
                "    private List<Role> roles = new ArrayList<>();\n" +
                "    private List<User> users = new ArrayList<>();\n" +
                "    private List<Address> addresses = new ArrayList<>();\n" +
                "    private List<UserProfile> userProfiles = new ArrayList<>();\n" +
                "    private List<History> histories = new ArrayList<>();\n" +
                "    private List<Category> categories = new ArrayList<>();\n" +
                "    private List<Food> foods = new ArrayList<>();\n" +
                "    private List<Combo> combos = new ArrayList<>();\n" +
                "    private List<Schedule> schedules = new ArrayList<>();\n" +
                "    private List<OrderDetail> orderDetails = new ArrayList<>();\n" +
                "    private List<Order> orders = new ArrayList<>();\n" +
                "\n" +
                "    @Override\n" +
                "    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {\n" +
                "        LOGGER.log(Level.INFO, String.format(\"Start seeding...\"));\n" +
                "        deleteAll();\n" +
                "        seedingRole();\n" +
                "        seedingUser();\n" +
                "        seedingUserProfile();\n" +
                "        seedingAddress();\n" +
                "        seedingCategory();\n" +
                "        seedingFood();\n" +
                "        seedingCombo();\n" +
                "        seedingSchedule();\n" +
                "        LOGGER.log(Level.INFO, String.format(\"Seeding success!\"));\n" +
                "    }\n" +
                "\n" +
                "    private void deleteAll() {\n" +
                "        historyRepository.deleteAll();\n" +
                "        userProfileRepository.deleteAll();\n" +
                "        addressRepository.deleteAll();\n" +
                "        orderDetailRepository.deleteAll();\n" +
                "        orderRepository.deleteAll();\n" +
                "        scheduleRepository.deleteAll();\n" +
                "        comboRepository.deleteAll();\n" +
                "        foodRepository.deleteAll();\n" +
                "        categoryRepository.deleteAll();\n" +
                "        userRepository.deleteAll();\n" +
                "        roleRepository.deleteAll();\n" +
                "\n" +
                "        historyRepository.resetIncrement();\n" +
                "        userProfileRepository.resetIncrement();\n" +
                "        addressRepository.resetIncrement();\n" +
                "        orderDetailRepository.resetIncrement();\n" +
                "        orderRepository.resetIncrement();\n" +
                "        scheduleRepository.resetIncrement();\n" +
                "        comboRepository.resetIncrement();\n" +
                "        foodRepository.resetIncrement();\n" +
                "        categoryRepository.resetIncrement();\n" +
                "        userRepository.resetIncrement();\n" +
                "        roleRepository.resetIncrement();" +
                "    }" +
                "\n";
    }

    private String getEndOfFile() {
        return "}\n ";
    }

    private String getFunctionSeedingRole() {
        return "\n" +
                "    private void seedingRole () {\n" +
                "        Role role;\n" +
                "        role = new Role(RoleName.ROLE_USER);\n" +
                "        roles.add(role);\n" +
                "\n" +
                "        role = new Role(RoleName.ROLE_ADMIN);\n" +
                "        roles.add(role);\n" +
                "\n" +
                "        roleRepository.saveAll(roles);\n" +
                "    }\n";
    }

    private String getFunctionSeedingUser() {
        String str = "    private void seedingUser() {\n" +
                "        User user;\n" +
                "        Role userRole;\n\n";
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Role roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN);
            String roleName;
            if (user.getRoles().contains(roleAdmin)) {
                roleName = "RoleName.ROLE_ADMIN";
            } else {
                roleName = "RoleName.ROLE_USER";
            }

            str += "        user  = new User();\n" +
                    "        user.setName(\"" + user.getName() + "\");\n" +
                    "        user.setUsername(\"" + user.getUsername() + "\");\n" +
                    "        user.setEmail(\"" + user.getEmail() + "\");\n" +
                    "        user.setPhone(\"" + user.getPhone() + "\");\n" +
                    "        user.setPassword(\"" + user.getPassword() + "\");\n" +
                    "        user.setStatus(" + user.getStatus() + ");\n" +
                    "        userRole = roleRepository.findByName(" + roleName + ");\n" +
                    "        user.setRoles(Collections.singleton(userRole));\n" +
                    "        users.add(user);\n\n";
        }
        str += "        userRepository.saveAll(users);\n" +
                "    }\n\n";

        return str;
    }

    private String getFunctionSeedingUserProfile() {
        String str = "    private void seedingUserProfile () {\n" +
                "        UserProfile userProfile;\n" +
                "        Optional<User> user;\n" +
                "        List<Category> categoryList;\n" +
                "        List<Long> categoryIds = new ArrayList<>();\n" +
                "        Set<Category> categorySet;\n" +
                "        \n";
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Set<UserProfile> userProfiles = user.getUserProfiles();
            for (UserProfile userProfile : userProfiles) {
                Set<Category> categorySet = userProfile.getCategories();
                for (Category category : categorySet) {
                    str += "        categoryIds.add((long) " + category.getId() + ");\n";
                }

                str += "\n" +
                        "        user = userRepository.findById((long) " + user.getId() + ");\n" +
                        "        categoryList = categoryRepository.findAllByIdIn(categoryIds);\n" +
                        "        categorySet = new HashSet<>(categoryList);\n" +
                        "        userProfile = new UserProfile();\n" +
                        "        userProfile.setHeight(" + userProfile.getHeight() + ");\n" +
                        "        userProfile.setWeight(" + userProfile.getWeight() + ");\n" +
                        "        userProfile.setYearOfBirth(" + userProfile.getYearOfBirth() + ");\n" +
                        "        userProfile.setGender(" + userProfile.getGender() + ");\n" +
                        "        userProfile.setExerciseIntensity(" + userProfile.getExerciseIntensity() + ");\n" +
                        "        userProfile.setBodyFat(" + userProfile.getBodyFat() + ");\n" +
                        "        userProfile.setBmrIndex(" + userProfile.getBmrIndex() + ");\n" +
                        "        userProfile.setLbmIndex(" + userProfile.getLbmIndex() + ");\n" +
                        "        userProfile.setTdeeIndex(" + userProfile.getTdeeIndex() + ");\n" +
                        "        userProfile.setStatus(" + userProfile.getStatus() + ");\n" +
                        "        userProfile.setCaloriesConsumed(" + userProfile.getCaloriesConsumed() + ");\n" +
                        "        userProfile.setUser(user.get());\n" +
                        "        userProfile.setCategories(categorySet);\n" +
                        "        userProfiles.add(userProfile);\n";
            }
        }
        str += "\n" +
                "        userProfileRepository.saveAll(userProfiles);\n" +
                "    }\n";

        return str;
    }

    private String getFunctionSeedingAddress() {
        String str = "    private void seedingAddress () {\n" +
                "        Address address;\n" +
                "        Optional<User> user;\n" +
                "\n";
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Set<Address> addressSet = user.getAddresses();
            for (Address address : addressSet) {

                str += "        user = userRepository.findById((long) " + address.getUser().getId() + ");\n" +
                        "        address = new Address();\n" +
                        "        address.setTitle(\"Ha Noi\");\n" +
                        "        address.setUser(user.get());\n" +
                        "        address.setStatus(1);\n" +
                        "        addresses.add(address);\n" +
                        "\n";
            }
        }
        str += "        addressRepository.saveAll(addresses);\n" +
                "    }\n\n";
        return str;
    }

    private String getFunctionSeedingCategory() {
        String str = "    private void seedingCategory () {\n" +
                "        Category category;\n" +
                "\n";

        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            str += "        category = new Category();\n" +
                    "        category.setName(\"" + category.getName() + "\");\n" +
                    "        category.setParentId((long) " + category.getParentId() + ");\n" +
                    "        category.setDescription(\"" + category.getDescription().replace("\n", "").replace("\r", "") + "\");\n" +
                    "        category.setImage(\"" + category.getImage() + "\");\n" +
                    "        category.setStatus(" + category.getStatus() + ");\n" +
                    "        categories.add(category);\n\n";
        }

        str += "        categoryRepository.saveAll(categories);\n" +
                "    }\n\n";
        return str;
    }

    private String getFunctionSeedingFood() {
        String str = "    private void seedingFood () {\n" +
                "        Food food;\n" +
                "        List<Category> categoryList;\n" +
                "        List<Long> categoryIds = new ArrayList<>();\n" +
                "        Set<Category> categorySet;\n" +
                "\n";

        List<Food> foods = foodRepository.findAll();
        for (Food food : foods) {
            Set<Category> categorySet = food.getCategories();
            for (Category category : categorySet) {
                str += "        categoryIds.add((long) " + category.getId() + ");\n" +
                        "\n";
            }

            str += "\n" +
                    "        categoryList = categoryRepository.findAllByIdIn(categoryIds);\n" +
                    "        categorySet = new HashSet<>(categoryList);\n" +
                    "        food = new Food();\n" +
                    "        food.setCategories(categorySet);\n" +
                    "        food.setName(\"" + food.getName() + "\");\n" +
                    "        food.setDescription(\"" + food.getDescription() + "\");\n" +
                    "        food.setImage(\"" + food.getImage() + "\");\n" +
                    "        food.setPrice((float)" + food.getPrice() + ");\n" +
                    "        food.setCarbonhydrates((float)" + food.getCarbonhydrates() + ");\n" +
                    "        food.setProtein((float)" + food.getProtein() + ");\n" +
                    "        food.setLipid((float)" + food.getLipid() + ");\n" +
                    "        food.setXenluloza((float)" + food.getXenluloza() + ");\n" +
                    "        food.setCanxi((float)" + food.getCanxi() + ");\n" +
                    "        food.setIron((float)" + food.getIron() + ");\n" +
                    "        food.setZinc((float)" + food.getZinc() + ");\n" +
                    "        food.setVitaminA((float)" + food.getVitaminA() + ");\n" +
                    "        food.setVitaminB((float)" + food.getVitaminB() + ");\n" +
                    "        food.setVitaminC((float)" + food.getVitaminC() + ");\n" +
                    "        food.setVitaminD((float)" + food.getVitaminD() + ");\n" +
                    "        food.setVitaminE((float)" + food.getVitaminE() + ");\n" +
                    "        food.setCalorie((float)" + food.getCalorie() + ");\n" +
                    "        food.setWeight((float)" + food.getWeight() + ");\n" +
                    "        food.setStatus(" + food.getStatus() + ");\n" +
                    "        food.setPrice((float)" + food.getPrice() + ");\n" +
                    "        foods.add(food);\n" +
                    "        categoryIds.clear();\n" +
                    "\n";
        }

        str += "        foodRepository.saveAll(foods);\n" +
                "    }\n\n";

        return str;
    }

    private String getFunctionSeedingCombo() {
        String str = "    private void seedingCombo () {\n" +
                "        Combo combo;\n" +
                "        List<Category> categoryList;\n" +
                "        List<Long> categoryIds = new ArrayList<>();\n" +
                "        Set<Category> categorySet;\n" +
                "\n";

        List<Combo> combos = comboRepository.findAll();
        for (Combo combo : combos) {
            Set<Category> categorySet = combo.getCategories();
            for (Category category : categorySet) {
                str += "        categoryIds.add((long) " + category.getId() + ");\n" +
                        "\n";
            }

            str += "        categoryIds.add((long) 1);\n" +
                    "\n" +
                    "        categoryList = categoryRepository.findAllByIdIn(categoryIds);\n" +
                    "        categorySet = new HashSet<>(categoryList);\n" +
                    "        combo = new Combo();\n" +
                    "        combo.setCategories(categorySet);\n" +
                    "        combo.setName(\"" + combo.getName() + "\");\n" +
                    "        combo.setImage(\"" + combo.getImage() + "\");\n" +
                    "        combo.setPrice((float)" + combo.getPrice() + ");\n" +
                    "        combo.setCarbonhydrates((float)" + combo.getCarbonhydrates() + ");\n" +
                    "        combo.setProtein((float)" + combo.getProtein() + ");\n" +
                    "        combo.setLipid((float)" + combo.getLipid() + ");\n" +
                    "        combo.setXenluloza((float)" + combo.getXenluloza() + ");\n" +
                    "        combo.setCanxi((float)" + combo.getCanxi() + ");\n" +
                    "        combo.setIron((float)" + combo.getIron() + ");\n" +
                    "        combo.setZinc((float)" + combo.getZinc() + ");\n" +
                    "        combo.setVitaminA((float)" + combo.getVitaminA() + ");\n" +
                    "        combo.setVitaminB((float)" + combo.getVitaminB() + ");\n" +
                    "        combo.setVitaminC((float)" + combo.getVitaminC() + ");\n" +
                    "        combo.setVitaminD((float)" + combo.getVitaminD() + ");\n" +
                    "        combo.setVitaminE((float)" + combo.getVitaminE() + ");\n" +
                    "        combo.setCalorie((float)" + combo.getCalorie() + ");\n" +
                    "        combo.setWeight((float)" + combo.getWeight() + ");\n" +
                    "        combo.setStatus(" + combo.getStatus() + ");\n" +
                    "        combo.setPrice((float)" + combo.getPrice() + ");\n" +
                    "        combos.add(combo);\n" +
                    "\n";
        }

        str += "        comboRepository.saveAll(combos);\n" +
                "    }\n\n";

        return str;
    }

    private String getFunctionSeedingSchedule() {
        String str = "    private void seedingSchedule() {\n" +
                "        Schedule schedule;\n";
        List<Schedule> schedules = scheduleRepository.findAll();
        for (Schedule schedule : schedules) {
            str += "        schedule = new Schedule();\n" +
                    "        schedule.setName(\"" + schedule.getName() +"\");\n" +
                    "        schedule.setDescription(\"" + schedule.getDescription() +"\");\n" +
                    "        schedule.setImage(\"" + schedule.getImage() + "\");\n" +
                    "        schedule.setPrice((float)" + schedule.getPrice() + ");\n" +
                    "        schedule.setStatus(" + schedule.getStatus() + ");\n" +
                    "        schedules.add(schedule);\n\n";
        }

        str += "        scheduleRepository.saveAll(schedules);\n"+
                "    }\n\n";
        return str;
    }

    private void createFile(String fileName) {
        try {
            File file = new File("src/main/java/com/spring/dev2chuc/nutritious_food/config/Seeding.java");
            //Ở đây mình tạo file trong ổ D
            boolean isCreat = file.createNewFile();
            if (isCreat)
                System.out.print("Da tao file thanh cong!");
            else
                System.out.print("Tao file that bai");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
