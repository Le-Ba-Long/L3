package com.globits.da.rest;

import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.service.CommuneService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/communes")
public class RestCommuneController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestCommuneController.class);
    @Autowired
    CommuneService communeService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseRequest<List<CommuneDto>> getAll() {
        List<CommuneDto> listDistrict = communeService.getAll();
        if (listDistrict.isEmpty()) {
            return new ResponseRequest<>(
                    ErrorMessage.LIST_IS_EMPTY.getCode(),
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    listDistrict);
        }
        return new ResponseRequest<>(
                ErrorMessage.SUCCESS.getCode(),
                ErrorMessage.SUCCESS.getMessage(),
                listDistrict);
    }

    @PostMapping("/insert")
    public ResponseRequest<CommuneDto> insert(@RequestBody CommuneDto communeDto) {
        String resultErrorMessage = communeService.insert(communeDto).getMessageError();
        int resultErrorCode = communeService.insert(communeDto).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<CommuneDto>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    communeDto);
        }
        return new ResponseRequest<CommuneDto>(
                resultErrorCode,
                resultErrorMessage,
                communeDto);
    }

    @PutMapping("/update/{id}")
    public ResponseRequest<CommuneDto> update(@PathVariable(name = "id") UUID id, @RequestBody CommuneDto communeDto) {
        String resultErrorMessage = communeService.update(id, communeDto).getMessageError();
        int resultErrorCode = communeService.update(id, communeDto).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<CommuneDto>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    communeDto);
        }
        return new ResponseRequest<CommuneDto>(
                resultErrorCode,
                resultErrorMessage,
                communeDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseRequest<CommuneDto> delete(@PathVariable("id") UUID id) {
        Commune commune = communeService.findById(id);
        if (communeService.deleteById(id)) {
            LOGGER.info("Delete Success");
            return new ResponseRequest<>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(), modelMapper.map(commune, CommuneDto.class));
        } else {
            LOGGER.info("Delete Not Success");
            return new ResponseRequest<>(ErrorMessage.ID_NOT_EXIST.getCode(),
                    ErrorMessage.ID_NOT_EXIST.getMessage(),
                    null);
        }
    }

    @GetMapping("/page")
    public Page<CommuneDto> getPage(@RequestParam(name = "pageIndex") Integer pageIndex, @RequestParam(name = "pageSize") Integer pageSize) {
        if (communeService.getPage(pageIndex, pageSize).isEmpty()) {
            throw new IllegalArgumentException("List Commune Is Empty");
        }
        return communeService.getPage(pageIndex, pageSize);
    }

}
