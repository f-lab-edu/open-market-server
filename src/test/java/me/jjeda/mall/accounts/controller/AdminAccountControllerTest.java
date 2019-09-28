package me.jjeda.mall.accounts.controller;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.common.BaseControllerTest;
import me.jjeda.mall.accounts.domain.Account;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.common.Address;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.accounts.repository.AccountRepository;
import me.jjeda.mall.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminAccountControllerTest extends BaseControllerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;


    //테스트를 동시에 수행하였을 때 사용자 중복으로 인한 오류를 피하기위해
    @Before
    public void setUp() {
        this.accountRepository.deleteAll();
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

    private String getAccessToken() throws Exception {
        // Given
        AccountDto admin = AccountDto.builder()
                .email("admin@admin.com")
                .password("pass")
                .accountRole(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .address(new Address("a", "b", "c"))
                .nickname("admin")
                .phone("01012341234")
                .build();
        accountService.saveAccount(admin);

        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("temp", "pass"))
                .param("username", admin.getEmail())
                .param("password",admin.getPassword())
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return "bearer " + parser.parseMap(responseBody).get("access_token").toString();
    }

    @Test
    @TestDescription("admin 이 아닌 상태에서 유효한 100명의 유저에서 20명씩 3번째 페이지 조회하기")
    public void queryAccountWithoutAdmin() throws Exception {
        // given
        IntStream.range(0, 100).forEach(this::generateAccount);

        // when & then
        this.mockMvc.perform(get("/admin/accounts")
                .param("page","2")
                .param("size","20")
                .param("sort","id,DESC")
                .param("isDeleted","false"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @TestDescription("admin 상태에서 유효한 100명의 유저에서 20명씩 3번째 페이지 조회하기")
    public void queryAccountWithAdmin() throws Exception {
        // given
        IntStream.range(0, 100).forEach(this::generateAccount);

        // when & then
        this.mockMvc.perform(get("/admin/accounts")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .param("page","2")
                .param("size","20")
                .param("sort","id,DESC")
                .param("isDeleted","false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.accountList[0]").exists());

    }

    @Test
    @TestDescription("관리자가 특정 사용자 조회하는 테스트")
    public void getAccountByAdmin() throws Exception {
        //given
        Account account = generateAccount(1);

        // when & then
        this.mockMvc.perform(get("/admin/accounts/{id}",account.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
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
    @TestDescription("관리자가 존재하지 않는 사용자 조회했을 때 찾지 못하는 테스트")
    public void getAccountByAdmin_Wrong_Account() throws Exception {
        //given
        Account account = generateAccount(1);

        // when & then
        this.mockMvc.perform(get("/admin/accounts/{id}",2312313L)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("관리자가 특정 사용자 삭제하는 테스트")
    public void deleteAccountByAdmin() throws Exception {
        //given
        Account account = generateAccount(1);

        // when & then
        this.mockMvc.perform(delete("/admin/accounts/{id}",account.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
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
    @TestDescription("관리자가 존재하지 않는 사용자 삭제했을 때 찾지 못하는 테스트")
    public void deleteAccountByAdmin_Wrong_Account() throws Exception {
        //given
        Account account = generateAccount(1);

        // when & then
        this.mockMvc.perform(delete("/admin/accounts/{id}",2312313L)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andExpect(status().isNotFound());
    }
}