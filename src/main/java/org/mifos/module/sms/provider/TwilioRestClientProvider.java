/**
 * Copyright 2014 Markus Geiss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mifos.module.sms.provider;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TwilioRestClientProvider implements SMSGateway {

    private static final Logger logger = LoggerFactory.getLogger(TwilioRestClientProvider.class);

    @Value("${mifos.smsgatewayprovider.accountid}")
    private String accountId;

    @Value("${mifos.smsgatewayprovider.authtoken}")
    private String authToken;

    @Value("${mifos.smsgatewayprovider.phoneno}")
    private String phoneNo;

    TwilioRestClientProvider() {
        super();
    }

    TwilioRestClient get() {
        final TwilioRestClient client = new TwilioRestClient(accountId, authToken);

        return client;
    }

    public boolean sendMessage(final String mobileNo, final String message) {
        final List<NameValuePair> messageParams = new ArrayList<NameValuePair>();
        messageParams.add(new BasicNameValuePair("From", this.phoneNo));
        messageParams.add(new BasicNameValuePair("To", "+" + mobileNo));
        messageParams.add(new BasicNameValuePair("Body", message));

        final TwilioRestClient twilioRestClient = this.get();
        final Account account = twilioRestClient.getAccount();
        final MessageFactory messageFactory = account.getMessageFactory();

        try {
            logger.info("Sending SMS to " + mobileNo + " ...");
            messageFactory.create(messageParams);
            return true;
        } catch (TwilioRestException trex) {
            logger.error("Could not send message, reason:", trex);
            return false;
        }
    }
}