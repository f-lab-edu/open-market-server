package me.jjeda.mall.accounts.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.common.BaseControllerTest;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.domain.Address;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import me.jjeda.mall.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends BaseControllerTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    //테스트를 동시에 수행하였을 때 사용자 중복으로 인한 오류를 피하기위해
    @Before
    public void setUp() {
        this.accountRepository.deleteAll();
    }

    @Test
    @TestDescription("정상적으로 계정을 생성하는 테스트")
    public void createAccount() throws Exception {
        //given
        AccountDto dto = AccountDto.builder()
                .email("jjeda@naver.com")
                .password("pass")
                .nickname("jjeda")
                .phone("010-1234-1234")
                .accountRole(Set.of(AccountRole.USER))
                .address(new Address("seoul", "anju", "1234"))
                .build();

        // when & then
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("nickname").value("jjeda"));

    }

    @Test
    @TestDescription("입력 값이 유효하지 않는 경우 에러가 발생하는 테스트")
    public void createAccount_Bad_Request_Wrong_Input() throws Exception {
        //given
        AccountDto dto = AccountDto.builder().build();

        // when & then
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    private Account generateAccount(int index) {
        AccountDto accountDto = AccountDto.builder()
                    .accountRole(Set.of(AccountRole.USER))
                    .address(new Address("a", "b", "c"))
                    .email("jjeda" + index + "@naver.com")
                    .nickname("jjeda" + index)
                    .phone("01012341234")
                    .password("pass")
                .build();
        return accountService.saveAccount(accountDto);

    }

    private String getAccessToken(Account account) throws Exception {
        // Given
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("temp", "pass"))
                .param("username", account.getEmail())
                .param("password", "pass")
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return "bearer "+ parser.parseMap(responseBody).get("access_token").toString();
    }

    @Test
    @TestDescription("로그인 후 자기정보 확인하는 테스트")
    public void getAccount() throws Exception {
        //given
        Account account = generateAccount(1);

        // when & then
        this.mockMvc.perform(get("/api/accounts/{id}",account.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(account)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("phone").exists())
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("isDeleted").value("false"));
    }

    @Test
    @TestDescription("다른유저의 정보를 확인하려할 때 401 메시지 받는 테스트")
    public void getAccount_401() throws Exception {
        //given
        Account account = generateAccount(1);
        Account account2 = generateAccount(2);

        // when & then
        this.mockMvc.perform(get("/api/accounts/{id}",account2.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(account)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("phone").exists())
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("isDeleted").value("false"));
    }

    @Test
    @TestDescription("정상적으로 개인정보를 변경하는 테스트")
    public void modifyAccount() throws Exception {
        //given
        Account account = generateAccount(1);

        AccountDto dto = AccountDto.builder()
                .email("update@naver.com")
                .password("update")
                .nickname("update")
                .phone("010-4321-4321")
                .accountRole(Set.of(AccountRole.USER))
                .address(new Address("seoul", "update", "1234"))
                .build();

        // when & then
        mockMvc.perform(put("/api/accounts/{id}",account.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto))
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(account)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("nickname").value("update"));


    }

    @Test
    @TestDescription("유효하지 않은 값으로 개인정보를 변경할때 Bad Request")
    public void modifyAccount_BadRequest_WrongInput() throws Exception {
        //given
        Account account = generateAccount(1);

        AccountDto dto = AccountDto.builder().build();

        // when & then
        mockMvc.perform(put("/api/accounts/{id}",account.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(dto))
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(account)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("정상적으로 계정정보를 삭제하는 테스트")
    public void deleteAccount() throws Exception {
        //given
        Account account = generateAccount(1);

        // when
        this.mockMvc.perform(delete("/api/accounts/{id}",account.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(account)))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        this.mockMvc.perform(get("/api/accounts/{id}",account.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(account)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("phone").exists())
                .andExpect(jsonPath("address").exists())
                .andExpect(jsonPath("isDeleted").value("true"));

    }

    @Test
    @TestDescription("다른유저의 계정정보를 삭제하려고 할때 401 메시지 받는 테스트")
    public void deleteAccount_401() throws Exception {
        //given
        Account account = generateAccount(1);
        Account account2 = generateAccount(2);

        // when
        this.mockMvc.perform(delete("/api/accounts/{id}",account2.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken(account)))
                .andDo(print())
                .andExpect(status().isUnauthorized());


    }

}