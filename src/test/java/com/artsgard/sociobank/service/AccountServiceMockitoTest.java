package com.artsgard.sociobank.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceMockitoTest {

    @Mock
    private SocioRepository socioRepo;

    @InjectMocks
    SocioServiceImpl socioService;

    @Mock
    private MapperService mapperService;

    private SocioModel socioModelMock1;
    private SocioModel socioModelMock2;
    private SocioDTO socioDTOMock1;
    private SocioDTO socioDTOMock2;
    private List<SocioModel> socioModelListMock;
    public static final Long NON_EXISTING_ID = 7000L;
    public static final Long EXISTING_ID = 1L;
    public static final String EXISTING_USERNAME = "username";
    public static final String NON_EXISTING_USERNAME = "SDFSDFSFSDFSDF";

    @BeforeEach
    public void setup() {
        LanguageModel lang1 = new LanguageModel(1L, "Netherlands", "NL");
        LanguageModel lang2 = new LanguageModel(1L, "Spain", "ES");
        List<LanguageModel> langs = new ArrayList();
        langs.add(lang1);
        langs.add(lang2);
        socioModelMock1 = new SocioModel(null, "username1", "secret1", "firstname1", "lastname1", "username1@gmail.com", true, langs, null);
        socioModelMock1.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socioModelMock1.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));

        socioModelMock2 = new SocioModel(null, "username2", "secret2", "firstname2", "lastname2", "username2@gmail.com", true, langs, null);
        socioModelMock2.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socioModelMock2.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));

        socioDTOMock1 = new SocioDTO(null, "username1", "secret1", "firstname1", "lastname1", "username1@gmail.com", true, langs, null);
        socioModelListMock = new ArrayList();
        socioModelListMock.add(socioModelMock1);
        socioModelListMock.add(socioModelMock2);
    }

    @Test
    public void findAllSociosTest() {
        given(socioRepo.findAll()).willReturn(socioModelListMock);
        given(mapperService.mapSocioModelToSocioDTO(any(SocioModel.class))).willReturn(socioDTOMock1);
        List<SocioDTO> list = socioService.findAllSocios();
        assertThat(list).isNotEmpty().hasSize(2);
    }

    @Test
    public void findAllSociosTest_not_found() {
        List<SocioModel> emptyList = new ArrayList();
        given(socioRepo.findAll()).willReturn(emptyList);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.findAllSocios();
        });
    }

    @Test
    public void findSocioByIdTest() {
        socioModelMock1.setId(EXISTING_ID);
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.of(socioModelMock1));
        given(mapperService.mapSocioModelToSocioDTO(any(SocioModel.class))).willReturn(socioDTOMock1);
        SocioDTO sc = socioService.findSocioById(any(Long.class));
        assertThat(sc).isNotNull();
        assertThat(sc.getUsername()).isEqualTo(socioModelMock1.getUsername());
    }

    @Test
    public void findSocioByIdTest_not_found() {
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.findSocioById(any(Long.class));
        });
    }

    @Test
    public void findSocioByUsernameTest() {
        socioModelMock1.setId(EXISTING_ID);
        given(socioRepo.findByUsername(EXISTING_USERNAME)).willReturn(Optional.of(socioModelMock1));
        given(mapperService.mapSocioModelToSocioDTO(any(SocioModel.class))).willReturn(socioDTOMock1);
        SocioDTO sc = socioService.findSocioByUsername(EXISTING_USERNAME);
        assertThat(sc).isNotNull();
        assertThat(sc.getUsername()).isEqualTo(socioModelMock1.getUsername());
    }

    @Test
    public void findSocioByUsernameTest_not_found() {
        given(socioRepo.findByUsername(EXISTING_USERNAME)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.findSocioByUsername(EXISTING_USERNAME);
        });
    }

    @Test
    public void saveSocioTest() {
        socioModelMock1.setId(EXISTING_ID);
        given(socioRepo.save(socioModelMock1)).willReturn(socioModelMock1);
        given(mapperService.mapSocioDTOToSocioModel(any(SocioDTO.class))).willReturn(socioModelMock1);
        SocioDTO sc = socioService.saveSocio(socioDTOMock1);
        assertThat(sc).isNotNull(); // why is this null!!!!!
    }

    @Test
    public void updateSocioTest() {
        socioDTOMock1.setId(EXISTING_ID);
        socioModelMock1.setId(EXISTING_ID);
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.of(socioModelMock1));
        given(socioRepo.save(socioModelMock1)).willReturn(socioModelMock1);
        given(mapperService.mapSocioDTOToSocioModel(any(SocioDTO.class))).willReturn(socioModelMock1);
        
        SocioDTO sc = socioService.updateSocio(socioDTOMock1);
        assertThat(sc).isNotNull(); // why is this null!!!!!
    }

    @Test
    public void updateSocioTest_not_found() {
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioDTOMock1.setId(any(Long.class));
            socioService.updateSocio(socioDTOMock1);
        });
    }

    @Test
    public void deleteSocioByIdTest() {
        socioRepo.deleteById(EXISTING_ID);
        verify(socioRepo, times(1)).deleteById(eq(EXISTING_ID));
    }

    @Test
    public void deleteSocioByIdTest_not_found() {
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.deleteSocioById(any(Long.class));
        });
    }

    @Test
    public void hasSocioByIdTest() {
        given(socioRepo.existsById(any(Long.class))).willReturn(true);
        Boolean flag = socioService.hasSocioById(any(Long.class));
        assertThat(flag).isTrue();
    }
    
    @Test
    public void hasSocioByIdTest_false() {
        given(socioRepo.existsById(any(Long.class))).willReturn(false);
        Boolean flag = socioService.hasSocioById(any(Long.class));
        assertThat(flag).isFalse();
    }

    @Test
    public void isSocioActiveByIdTest() {
        socioModelMock1.setId(EXISTING_ID);
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.of(socioModelMock1));
        socioModelMock1.setActive(Boolean.TRUE);
        Boolean flag = socioService.isSocioActiveById(any(Long.class));
         assertThat(flag).isTrue();
    }

    @Test
    public void isSocioActiveByIdTest_not_found() {
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.empty());
         Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.isSocioActiveById(any(Long.class));
        });
    }

    //@Test
    public void testGetSociosBySortedPageByCountry() {
        FilterDTO filter = new FilterDTO();
        filter.setCountry("NL");
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByCountry(10, 0, filter.getCountry());
        for (SocioModel sc : socios) {
            for (AddressModel add : sc.getSocioAddresses()) {
                assertThat(add.getCountry()).isEqualTo(new CountryModel(1L, "Netherlands", "NL"));
            }

        }
        assertThat(socios).isNotEmpty();
        assertThat(socios).hasSize(2);

    }

    //@Test
    public void testGetSociosBySortedPageByCountry_not_found() {
        FilterDTO filter = new FilterDTO();
        filter.setCountry("NLxxxxx");
        socioRepo.deleteAll();
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByCountry(10, 0, filter.getCountry());
        assertThatExceptionOfType(ResourceNotFoundException.class);

    }

    //@Test
    public void testGetSociosBySortedPageByLanguage() {
        FilterDTO filter = new FilterDTO();
        filter.setLanguage("FR");
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByLanguage(3, 0, filter.getLanguage());
        for (SocioModel sc : socios) {
            assertThat(sc.getSocioLanguages()).contains(new LanguageModel(3L, "France", "FR"));
        }
        assertThat(socios).isNotEmpty();

    }

    //@Test
    public void testGetSociosBySortedPageByLanguage_not_found() {
        FilterDTO filter = new FilterDTO();
        filter.setLanguage("FRxxxxx");
        socioRepo.deleteAll();
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByLanguage(3, 0, filter.getLanguage());
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testGetSociosBySortedPage() {
        List<SocioModel> socios = socioRepo.getSociosBySortedPage(2, 0);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testGetSociosBySortedPage_not_found() {
        socioRepo.deleteAll();
        List<SocioModel> socios = socioRepo.getSociosBySortedPage(2, 0);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
}