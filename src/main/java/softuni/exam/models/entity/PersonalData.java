package softuni.exam.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Entity
@Table(name = "personal_datas")
public class PersonalData extends BaseEntity {

    @Column(nullable = true)
    private Integer age;

    @Column(name = "birth_date", nullable = true)
    @Past
    private LocalDate birthDate;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = true)
    @Pattern(regexp = "[MF]")
    private String gender;

//    @OneToOne(optional = false)
//    private Visitor visitor;

    public PersonalData() {
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//    public Visitor getVisitor() {
//        return visitor;
//    }
//
//    public void setVisitor(Visitor visitor) {
//        this.visitor = visitor;
//    }
}
