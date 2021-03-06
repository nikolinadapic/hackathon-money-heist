package com.ag04.sbss.hackathon.app.forms;

import com.ag04.sbss.hackathon.app.model.Sex;
import com.ag04.sbss.hackathon.app.model.StatusMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Vitomir M on 22.8.2020.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {

        @NotBlank
        private String name;

        @NotBlank
        private Sex sex;

        @Email
        private String email;

        @NotNull
        private List<MemberSkillDTO> skills;

        private String mainSkill;

        @NotNull
        private StatusMember status;
}
