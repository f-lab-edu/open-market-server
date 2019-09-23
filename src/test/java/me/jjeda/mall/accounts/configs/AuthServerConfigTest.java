package me.jjeda.mall.accounts.configs;

import me.jjeda.mall.accounts.Service.AccountService;
import me.jjeda.mall.accounts.domain.AccountRole;
import me.jjeda.mall.accounts.domain.Address;
import me.jjeda.mall.accounts.dto.AccountDto;
import me.jjeda.mall.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthServerConfigTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private AccountService accountService;


    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        //given
        AccountDto accountDto = AccountDto.builder()
                .accountRole(Set.of(AccountRole.USER))
                .address(new Address("a", "b", "c"))
                .email("jjeda@naver.com")
                .nickname("jjeda")
                .phone("01012341234")
                .password("pass")
                .build();
        accountService.saveAccount(accountDto);

        //when & then
        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic("temp", "pass"))
                .param("username", accountDto.getEmail())
                .param("password", accountDto.getPassword())
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}