package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.services.UsersService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@SpringBootTest
public class TweetControllerTest {

    @Mock
    UsersService usersService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void addTweetTest() throws Exception{
        String userName="siva@gmail.com";
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/add")
                        .sessionAttr("userName",userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tweetText\":\"welcome\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="Tweet created";
        assertEquals(expected,actual);
    }

    @Test
    public void addUserNotFoundTweetTest() throws Exception{
        String userName="siv@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/add")
                        .sessionAttr("userName",userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tweetText\":\"welcome\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="User name not found";
        assertEquals(expected,actual);
    }

    @Test
    public void getAllTweetsTest() throws Exception{
        MvcResult result1 = mockMvc
                .perform(get("http://localhost:8080/api/v1.0/tweets/all"))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        assertEquals(actual,actual);
    }

    @Test
    public void getUsersTweetsTest() throws Exception{
        String userName="siva@gmail.com";
        MvcResult result1 = mockMvc
                .perform(get("http://localhost:8080/api/v1.0/tweets/"+userName))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        assertEquals(actual,actual);
    }

    @Test
    public void getValidUsersTweetsTest() throws Exception{
        String userName="siv@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(get("http://localhost:8080/api/v1.0/tweets/"+userName))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="User name not found";
        assertEquals(expected,actual);
    }

    @Test
    public void updateTweetsTest() throws Exception{
        String userName="siv@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(put("http://localhost:8080/api/v1.0/tweets/"+userName+"/update/a1331033-f4ac-496a-9092-79366d960750")
                        .sessionAttr("userName",userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tweetText\":\"welcome\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="User name not found";
        assertEquals(expected,actual);
    }

    @Test
    public void updateTweetsIdNotFoundTest() throws Exception{
        String userName="siva@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(put("http://localhost:8080/api/v1.0/tweets/"+userName+"/update/90798d7f-f4e6-4ace-b2ee").sessionAttr("userName",userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tweetText\":\"welcome\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="Given tweetId cannot be found";
        assertEquals(expected,actual);
    }

    @Test
    public void deleteTweetTest() throws Exception{
        String userName="siva@gmail.com";
        MvcResult result1 = mockMvc
                .perform(delete("http://localhost:8080/api/v1.0/tweets/"+userName+"/delete/a1331033-f4ac-496a-9092-79366d960750")
                        .sessionAttr("userName",userName))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="Tweet deleted successfully";
        assertEquals(expected,actual);
    }

    @Test
    public void deleteTweetNotFoundTest() throws Exception{
        String userName="siva@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(delete("http://localhost:8080/api/v1.0/tweets/"+userName+"/delete/c20b1bc8-02cc-4586-a55a-5adcd178c4f5")
                        .sessionAttr("userName",userName))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="Given tweetId cannot be found";
        assertEquals(expected,actual);
    }

    @Test
    public void likeTweetTest() throws Exception{
        String userName="siva@gmail.com";
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/like/a1331033-f4ac-496a-9092-79366d960750")
                        .sessionAttr("userName",userName))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="liked tweet";
        assertEquals(expected,actual);
    }

    @Test
    public void userNameNotFoundTest() throws Exception{
        String userName="siv@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/like/a1331033-f4ac-496a-9092-79366d960750")
                        .sessionAttr("userName",userName))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="User name not found";
        assertEquals(expected,actual);
    }

    @Test
    public void likeTweetNotFoundTest() throws Exception{
        String userName="siva@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/like/c20b1bc8-02cc-4586-a55a")
                        .sessionAttr("userName",userName))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="Given tweetId cannot be found";
        assertEquals(expected,actual);
    }

    @Test
    public void replyTweetTest() throws Exception{
        String userName="siva@gmail.com";
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/reply/a1331033-f4ac-496a-9092-79366d960750")
                        .sessionAttr("userName",userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tweetText\":\"welcome\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="Replied";
        assertEquals(expected,actual);
    }

    @Test
    public void replyUserNameNotFoundTest() throws Exception{
        String userName="siv@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/reply/a1331033-f4ac-496a-9092-79366d960750")
                        .sessionAttr("userName",userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tweetText\":\"welcome\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="User name not found";
        assertEquals(expected,actual);
    }

    @Test
    public void replyTweetNotFoundTest() throws Exception{
        String userName="siva@gmail.com";
        when(usersService.getByUserName(userName)).thenReturn(null);
        MvcResult result1 = mockMvc
                .perform(post("http://localhost:8080/api/v1.0/tweets/"+userName+"/reply/c20b1bc8-02cc-4586-a55a")
                        .sessionAttr("userName",userName)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"tweetText\":\"welcome\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String actual=result1.getResponse().getContentAsString();
        String expected="Given tweetId cannot be found";
        assertEquals(expected,actual);
    }
}

