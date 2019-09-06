package me.jjeda.mall.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.domain.Address;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import me.jjeda.mall.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @TestDescription("정상적으로 계정을 생성하는 테스트")
    public void createAccount() throws Exception {
        //given
        AccountDto dto = AccountDto.builder()
                .email("jjeda@naver.com")
                .password("pass")
                .userName("jjeda")
                .phone("010-1234-1234")
                .accountRole(AccountRole.USER)
                .address(new Address("seoul", "anju", "1234"))
                .build();
        accountRepository.save(dto.toEntity());

        // when & then
        mockMvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("userName").value("jjeda"));
//                .andExpect(jsonPath("password").doesNotExist());
    }

    @Test
    @TestDescription("입력 값이 유효하지 않는 경우 에러가 발생하는 테스트")
    public void createAccount_Bad_Request_Wrong_Input() {

    }

    @Test
    @TestDescription("30명의 유저에서 10명씩 2번째 페이지 조회하기")
    public void queryAccount() {

    }

    @Test
    @TestDescription("정상적으로 유저 한명을 조회하는 테스트")
    public void getAccount() {

    }

    @Test
    @TestDescription("정상적으로 비밀번호를 변경하는 테스트")
    public void modifyPassword() {

    }

    @Test
    @TestDescription("정상적으로 개인정보를 변경하는 테스트")
    public void modifyAccount() {

    }

    @Test
    @TestDescription("정상적으로 계정정보를 삭제하는 테스트")
    public void deleteAccount() {

    }

}