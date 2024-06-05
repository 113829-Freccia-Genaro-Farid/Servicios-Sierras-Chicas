package tesis.controllers;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tesis.dtos.auxiliar.UserBuyer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class MercadoPagoController {

    @Value("APP_USR-6213206895047798-053118-eabc908c8a32b51db842002513045f16-1831939291")
    private String codigoMercadoLibre;


    @PostMapping("/api/mp")
    public String getList(@RequestBody UserBuyer userBuyer){
        if (userBuyer == null) return "No se ha podido realizar la compra";
        String title = userBuyer.getTitle();
        int quantity = userBuyer.getQuantity();
        int price = userBuyer.getPrice();

        try {
            MercadoPagoConfig.setAccessToken(codigoMercadoLibre);

            // Preferencia de venta
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(title)
                    .quantity(quantity)
                    .unitPrice(BigDecimal.valueOf(price))
                    .currencyId("ARS")
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            // Preferencia de control de sucesos
            PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:4200/home")
                    .failure("http://localhost:4200/datospersonales")
                    .build();

            // Preferencia general ENSAMBLE DE PREFERENCIAS
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .operationType("regular_payment")
                    .binaryMode(true)
                    .backUrls(backUrlsRequest)
                    .notificationUrl("https://9aac-152-171-83-111.ngrok-free.app/webhook")
                    .autoReturn("approved")
                    .build();

            //Creo un objeto tipo cliente para comunicarme con MP
            PreferenceClient preferenceClient = new PreferenceClient();
            Preference preference = preferenceClient.create(preferenceRequest);

            //retornamos esa preferencia a nuestro front
            return preference.getId();
        } catch (MPException | MPApiException e) {
            e.printStackTrace();
            return "No se ha podido realizar la compra";
        }
    }
}