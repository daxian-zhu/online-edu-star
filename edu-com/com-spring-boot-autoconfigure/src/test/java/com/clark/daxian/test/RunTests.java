package com.clark.daxian.test;

import com.clark.daxian.Run;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Run.class)
@EnableAutoConfiguration
public class RunTests {


}
