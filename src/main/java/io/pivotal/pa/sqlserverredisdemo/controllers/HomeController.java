package io.pivotal.pa.sqlserverredisdemo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping()
    public ResponseEntity<StatusInfo> getIndex() {
        return ResponseEntity.ok(
            new StatusInfo(
                redisTemplate.getClientList(),
                jdbcTemplate.queryForList("SELECT name FROM SYSOBJECTS WHERE xtype='U'",String.class)
            )
        );
    }

    public class StatusInfo {
        List<RedisClientInfo> clients = new ArrayList<RedisClientInfo>(0);
        List<String> tableNames = new ArrayList<String>(0);
    
        public StatusInfo(List<RedisClientInfo> clients, List<String> tableNames) {
            this.clients = clients;
            this.tableNames = tableNames;
        }
    
        public List<RedisClientInfo> getClients() {
            return clients;
        }
    
        public void setClients(List<RedisClientInfo> clients) {
            this.clients = clients;
        }

        public List<String> getTableNames() {
            return tableNames;
        }

        public void setTableNames(List<String> tableNames) {
            this.tableNames = tableNames;
        }
    }
}