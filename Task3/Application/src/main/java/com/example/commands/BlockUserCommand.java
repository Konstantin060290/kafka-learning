package com.example.commands;

import org.apache.kafka.common.security.oauthbearer.internals.secured.AccessTokenRetriever;
import shortbus.Request;

public class BlockUserCommand implements Request<Boolean> {
    public String userName;
    public String blockedUser;

    public BlockUserCommand(String userName, String BlockedUser)
    {
        this.userName = userName;
        this.blockedUser = BlockedUser;
    }
}
