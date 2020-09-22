package mk.finki.ukim.mk.map_application.bootstrap;

import lombok.Getter;
import mk.finki.ukim.mk.map_application.model.Auth.Role;
import mk.finki.ukim.mk.map_application.model.Auth.RoleName;
import mk.finki.ukim.mk.map_application.model.Auth.User;
import mk.finki.ukim.mk.map_application.model.Category;
import mk.finki.ukim.mk.map_application.model.Map;
import mk.finki.ukim.mk.map_application.model.Pin;
import mk.finki.ukim.mk.map_application.model.Visibility;
import mk.finki.ukim.mk.map_application.repository.Auth.RoleRepository;
import mk.finki.ukim.mk.map_application.repository.Auth.UserRepository;
import mk.finki.ukim.mk.map_application.repository.MapsJPARepository;
import mk.finki.ukim.mk.map_application.repository.PinsJPARepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Getter
public class DataHolder {

    private static final List<Pin> pins = new ArrayList<>();
    private static final List<Map> maps = new ArrayList<>();
    private static final List<Role> roles = new ArrayList<>();
    private static final List<User> users = new ArrayList<>();

    private final PinsJPARepository pinsJPARepository;
    private final MapsJPARepository mapsJPARepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    DataHolder(PinsJPARepository pinsJPARepository, MapsJPARepository mapsJPARepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.mapsJPARepository = mapsJPARepository;
        this.pinsJPARepository = pinsJPARepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }


    @PostConstruct
    public void init() {

        User user = new User("Vladimir Blazheski", "vblazhes", "vladimirblazeski@yahoo.com", "$2a$10$wWyYbomFlq8D4B2aT4AtZuChfXY8dxLJH7UwIfh4SwZMTTjXG.CIW");
        User user1 = new User("Admin", "admin", "admin.admin@yahoo.com", "$2a$10$wWyYbomFlq8D4B2aT4AtZuChfXY8dxLJH7UwIfh4SwZMTTjXG.CIW");

        users.add(0, user);
        users.add(1,user1);

        //maps.add(0, new Map(1 ,10, 41.123096, 20.801647, "mapbox://styles/vblazhes/ck62e0e7k0z1p1in0fy9cb2ke", "Map of Churches in city of Ohrid ", "https://lh5.googleusercontent.com/p/AF1QipOvqd3bUJkLMwUSR5u1Wf4HbKg3b5LY_MbiU8Sd=w408-h291-k-no","Description for Ohrid Church Map", Visibility.PUBLIC,new ArrayList<>(), null ));
        maps.add(0, new Map(1 ,10, 41.123096, 20.801647, "mapbox://styles/vblazhes/ck62e0e7k0z1p1in0fy9cb2ke", "Map of Churches in city of Ohrid ", "https://img.theculturetrip.com/wp-content/uploads/2018/06/shutterstock_319995938.jpg","Description for Ohrid Church Map", Visibility.PUBLIC,1,0,new ArrayList<>(),null ));
        maps.add(1, new Map(2,10,41.123096, 20.801647,"mapbox://styles/vblazhes/ck6b9ro1u18ug1ikvbju56691","Map of Hotels in city of Ohrid","https://lh5.googleusercontent.com/p/AF1QipM8LK82bOd3WAZZef2IEhRNMOWuyBg8sjObgkD5=w493-h240-k-no","Description for Ohrid Hotel Map",Visibility.PUBLIC,1,0,new ArrayList<>() ,null));
//        maps.add(2, new Map(3,10,41.123096, 20.801647,"mapbox://styles/vblazhes/ck62e0e7k0z1p1in0fy9cb2ke","Map of Monuments in Paris","https://lh5.googleusercontent.com/p/AF1QipP8E1nOUwx73CrO0pnZzHTk_O3dTyfzbN6aWnYt=w408-h256-k-no","Description for Monuments in Paris Map",Visibility.PUBLIC,1,0,new ArrayList<>() ,null));
//        maps.add(3, new Map(4,10,41.123096, 20.801647,"mapbox://styles/vblazhes/ck6b9ro1u18ug1ikvbju56691","Map of Lakes in Switzerland","https://lh5.googleusercontent.com/p/AF1QipNCP73hn9ODnjNT4-vhmHoXMJxRfRQ8EMdX8EjP=w408-h257-k-no","Description for Lakes in Switzerland",Visibility.PUBLIC,1,0,new ArrayList<>() ,null));


        pins.add(0, new Pin(1, "Church St.Jovan at Kaneo", 41.111098, 20.788765, "Saint John the Theologian, Kaneo or simply Saint John at Kaneo is a Macedonian Orthodox church situated on the cliff over Kaneo Beach overlooking Lake Ohrid in the city of Ohrid, North Macedonia. The church is dedicated to John of Patmos, the writer of Revelation, who has been by some considered to be the same person as John the Apostle. The construction date of the church remains unknown but documents detailing the church property suggest that it was built before the year 1447. Archaeologists believe that the church was constructed some time before the rise of the Ottoman Empire very likely in the 13th century. Restoration work in 1964 led to the discovery of frescoes in its dome.", Category.CHURCH, "https://lh5.googleusercontent.com/p/AF1QipPcI_MX3LmxrWFsn-H0PAfbGIg_kPrCIDlwyGel=w426-h240-k-no", null));
        pins.add(1, new Pin(2, "Monastery St. Naum", 40.913921, 20.740665, "The Monastery of Saint Naum (Macedonian: Манастир „Свети Наум“) is an Eastern Orthodox monastery in North Macedonia, named after the medieval Saint Naum who founded it. It is situated along Lake Ohrid, 29 kilometres (18 mi) south of the city of Ohrid.\nThe Lake Ohrid area, including St Naum, is one of the most popular tourist destinations in North Macedonia.\n", Category.CHURCH, "https://lh5.googleusercontent.com/p/AF1QipOkE3N1aXwHyqU-Kyffg_q2MRDBFHvsjSN0AIKN=w408-h306-k-no", null));
        pins.add(2, new Pin(3, "Church of St.Spas", 41.139810, 20.838170, "The church of St.Spas is the most famous and most explored church in Ohrid area during the 20th century. The church is located in the village of Leskoec, on the slopes of Mount Petrino, in the immediate vicinity of Ohrid. ", Category.CHURCH, "https://mymacedoniablog.files.wordpress.com/2018/04/dv-174-lescoec-to-velgosti-hike-st-spas.jpg?w=1024", null));
        pins.add(3, new Pin(4, "Hotel Metropol", 41.057524, 20.801766, "Metropol Lake Resort - Ohrid is located at the far end of the south-west part of the Republic of Macedonia, in the city of Ohrid, distanced only 172 km, from the capital - Skopje. This region is well known tourist destination in our country, easily accessible for all domestic and foreign tourists.", Category.HOTEL, "https://lh5.googleusercontent.com/p/AF1QipPGzfOFsoD21LVLQhcP6KQPRZwTg0-SXf_YjSFd=w408-h271-k-no", null));
        pins.add(4,new Pin(5,"Hotel Inex Gorica",41.084798, 20.797210,"Inex Olgica Hotel & SPA is situated on the shore of the Ohrid Lake, 4 km away from Ohrid and a 10-minute drive from Ohrid Airport.",Category.HOTEL,"https://lh5.googleusercontent.com/proxy/Je4zUAoAbRG9zKGg_yRvHaJIwCA5yxGzEL2QXPi1p8ZyUlYxQAxL9giXt_K1gx-CLkAgY4d1zO9ny9aCB9LAUFr-SXWYNzMluqzHCQOSLZdG_Gd2MZxLY3j_L6-LQvVo0Oej-U2jXBQdtDy2rLDdTUrsoHXEEyw=w408-h272-k-no",null));

        roles.add(0, new Role(RoleName.ROLE_USER));
        roles.add(1, new Role(RoleName.ROLE_ADMIN));

        Set<Role> role_user = new HashSet<>();
        role_user.add(roles.get(0));
        Set<Role> role_admin = new HashSet<>();
        role_admin.add(roles.get(1));

        user1.setRoles(role_admin);
        user.setRoles(role_user);

        if(this.mapsJPARepository.count() == 0){
            this.pinsJPARepository.saveAll(pins);
            this.mapsJPARepository.saveAll(maps);
            this.roleRepository.saveAll(roles);
            this.userRepository.saveAll(users);

            maps.get(0).setOwner(users.get(0));
            maps.get(1).setOwner(users.get(0));

            pins.get(0).setMap(maps.get(0));
            pins.get(1).setMap(maps.get(0));
            pins.get(2).setMap(maps.get(0));

            pins.get(3).setMap(maps.get(1));
            pins.get(4).setMap(maps.get(1));

            this.pinsJPARepository.saveAll(pins);
            this.mapsJPARepository.saveAll(maps);
            this.userRepository.saveAll(users);
        }
    }

}
