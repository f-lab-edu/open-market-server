package me.jjeda.mall.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.jjeda.mall.accounts.domain.Account;
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

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    }

    @Test
    @TestDescription("입력 값이 유효하지 않는 경우 에러가 발생하는 테스트")
    public void createAccount_Bad_Request_Wrong_Input() {

    }

    @Test
    @TestDescription("유효한 100명의 유저에서 20명씩 3번째 페이지 조회하기")
    public void queryAccount() throws Exception {
        // given
        IntStream.range(0, 100).forEach(this::generateAccount);

        // when & then
        this.mockMvc.perform(get("/api/accounts/admin")
                    .param("page","2")
                    .param("size","20")
                    .param("sort","id,DESC")
                    .param("isDeleted","false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.accountList[0]").exists());

    }

    private Account generateAccount(int index) {
        AccountDto accountDto = AccountDto.builder()
                    .accountRole(AccountRole.USER)
                    .address(new Address("", "", ""))
                    .email("jjeda" + index + "@naver.com")
                    .userName("jjeda" + index)
                    .phone("01012341234")
                    .password("pass")
                .build();
        return accountRepository.save(accountDto.toEntity());

    }

    @Test
    @TestDescription("정상적으로 유저 한명을 조회하는 테스트")
    public void getAccount() throws Exception {
        //given
        Account account = generateAccount(1);

        // when & then
        this.mockMvc.perform(get("/api/accounts/{id}",account.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("userName").exists())
                .andExpect(jsonPath("phone").exists())
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("isDeleted").value("false"));
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
    public void deleteAccount() throws Exception {
        //given
        Account account = generateAccount(1);

        // when & then
        this.mockMvc.perform(delete("/api/accounts/{id}",account.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/api/accounts/{id}",account.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("userName").exists())
                .andExpect(jsonPath("phone").exists())
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("isDeleted").value("true"));

    }

}