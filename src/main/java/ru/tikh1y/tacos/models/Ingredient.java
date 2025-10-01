package ru.tikh1y.tacos.models;
import lombok.Data;


//@Table("ingredient")
//@Data
//@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
//@AllArgsConstructor
//public class Ingredient implements Persistable<String> {
//    @Id
//    private final String id;
//    private final String name;
//    private final Type type;
//
//    @Override
//    public boolean isNew() {
//        return id == null;
//    }
//}


@Data
public class Ingredient{
    private final String id;
    private final String name;
    private final Type type;
}
