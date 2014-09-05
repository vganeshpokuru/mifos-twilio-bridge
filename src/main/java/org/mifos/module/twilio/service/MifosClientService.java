package org.mifos.module.twilio.service;

import org.mifos.module.twilio.domain.Client;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

public interface MifosClientService {

    @GET("/clients/{id}")
    public Client findClient(@Header("Authorization") String authorization,
                             @Header("X-Mifos-Platform-TenantId") String tenantIdentifier,
                             @Path("id") final long id);
}
