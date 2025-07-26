package org.example.websitetechworld.Controller.ClientController.DiemController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Services.ClientServices.DiemServices.ViDiemServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/viDiem")
@RequiredArgsConstructor
public class ViDiemControler {
    private final ViDiemServices viDiemServices;

    //public
}
