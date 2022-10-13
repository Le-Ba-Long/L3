package com.globits.da.rest;

import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.service.CommuneService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.globits.da.common.ErrorMessage.*;

@RestController
@RequestMapping("/api/communes")
public class RestCommuneController {
    @Autowired
    private CommuneService communeService;
    @GetMapping("/list")
    public ResponseData<List<CommuneDto>> getAll() {
        List<CommuneDto> listDistrict = communeService.getAll();
        if (listDistrict.isEmpty())
            return new ResponseData<>(LIST_IS_EMPTY, listDistrict);
        return new ResponseData<>(listDistrict);
    }

    @PostMapping()
    public ResponseData<CommuneDto> insert(@RequestBody CommuneDto communeDto) {
        ResponseData<CommuneDto> errMessage = communeService.insert(communeDto);
        if (errMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errMessage.getData());
        return new ResponseData<>(errMessage.getStatusCode(), errMessage.getMessageError(), null);
    }

    @PutMapping("/{id}")
    public ResponseData<CommuneDto> update(@PathVariable(name = "id") UUID id, @RequestBody CommuneDto communeDto) {
        ResponseData<CommuneDto> errMessage = communeService.update(id, communeDto);
        if (errMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errMessage.getData());
        return new ResponseData<>(errMessage.getStatusCode(), errMessage.getMessageError(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseData<CommuneDto> delete(@PathVariable("id") UUID id) {
        Commune commune = communeService.findById(id);
        if (communeService.deleteById(id))
            return new ResponseData<>(CommuneDto.toDto(commune));
        return new ResponseData<>(ID_NOT_EXIST, null);
    }

    @GetMapping("/page")
    public Page<CommuneDto> getPage(@RequestParam(name = "pageIndex") Integer pageIndex, @RequestParam(name = "pageSize") Integer pageSize) {
        if (communeService.getPage(pageIndex, pageSize).isEmpty()) {
            throw new IllegalArgumentException("List Commune Is Empty");
        }
        return communeService.getPage(pageIndex, pageSize);
    }
}
