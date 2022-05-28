package com.infybuzz.feignclients;

import com.infybuzz.response.AddressResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Using open feign with Eureka Server
//@FeignClient(value = "address-service", path = "/address")
@FeignClient(name = "address-service", fallbackFactory = AddressFallBackFactory.class) // 2 kere filter classlarına düşer

// Using open feign with Api Gateway
//@FeignClient(value = "api-gateway", fallback=AddressFallback.class)
//@FeignClient(value = "api-gateway", fallbackFactory = AddressFallBackFactory.class) // 2 kere filter classlarına düşer
public interface AddressFeignClient {

    // Using open feign with Eureka Server
    @GetMapping("/address/getById/{id}")
    AddressResponse getById(@PathVariable long id);

    // Using open feign with Api Gateway
   /* @GetMapping("/address-service/address/getById/{id}")
    AddressResponse getById(@PathVariable long id);*/
}

/*@Component
class AddressFallback implements AddressFeignClient {

    @Override
    public AddressResponse getById(long id) {
        return new AddressResponse();
    }

}*/

@Component
class AddressFallBackFactory implements FallbackFactory<AddressFeignClient> {

    @Override
    public AddressFeignClient create(Throwable cause) {
        return new AddressServiceClientFallBack(cause);
    }
}

class AddressServiceClientFallBack implements AddressFeignClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Throwable cause;

    public AddressServiceClientFallBack(Throwable cause) {
        this.cause = cause;
    }


    @Override
    public AddressResponse getById(long id) {
        if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
            logger.error("404 error took place when getById was called with address ID: " + id + ". Error message: "
                    + cause.getLocalizedMessage());
        } else logger.error("Other error took place: " + cause.getLocalizedMessage());

        System.out.println(cause.toString());
        return new AddressResponse();
    }
}
