package dev.alejandro.heroquest_backend.hero;

import dev.alejandro.heroquest_backend.auth.model.User;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTORequest;
import dev.alejandro.heroquest_backend.hero.dto.HeroDTOResponse;
import dev.alejandro.heroquest_backend.hero.dto.HeroUpdateStatsDTO;
import dev.alejandro.heroquest_backend.hero.mapper.HeroMapper;
import dev.alejandro.heroquest_backend.hero.model.Hero;
import dev.alejandro.heroquest_backend.hero.repository.HeroRepository;
import dev.alejandro.heroquest_backend.hero.service.HeroServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HeroServiceImplTest {

    @InjectMocks
    private HeroServiceImpl heroService;

    @Mock
    private HeroRepository heroRepository;

    @Mock
    private HeroMapper heroMapper;

    private User user;
    private Hero hero;
    private HeroDTORequest dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creamos usuario de prueba
        user = User.builder().id(1L).username("testuser").build();

        // Creamos héroe de prueba
        hero = Hero.builder()
                .id(1L)
                .heroClass("Barbarian")
                .health(12)
                .attack(14)
                .defense(8)
                .movement(6)
                .experiencia(0)
                .nivel(1)
                .user(user)
                .build();

        // DTO de creación
        dto = new HeroDTORequest();
        dto.setHeroClass("Barbarian");
    }

    @Test
    void createHero_ShouldReturnHeroDTO_WhenUserHasNoHero() {
        when(heroRepository.existsByUser(user)).thenReturn(false);
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);

        // Simulamos respuesta sin puntosRestantes
        when(heroMapper.toHeroDTOResponse(hero)).thenReturn(
                HeroDTOResponse.builder()
                        .id(1L)
                        .heroClass("Barbarian")
                        .health(12)
                        .attack(14)
                        .defense(8)
                        .movement(6)
                        .experiencia(0)
                        .nivel(1)
                        .build()
        );

        HeroDTOResponse response = heroService.createHero(user, dto);

        assertNotNull(response);
        assertEquals("Barbarian", response.getHeroClass());
        verify(heroRepository).save(any(Hero.class));
    }

    @Test
    void createHero_ShouldThrowException_WhenUserAlreadyHasHero() {
        when(heroRepository.existsByUser(user)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> heroService.createHero(user, dto));
        assertEquals("This user already has a hero assigned", ex.getMessage());
        verify(heroRepository, never()).save(any(Hero.class));
    }

    @Test
    void getHeroByUser_ShouldReturnHeroDTO_WhenHeroExists() {
        when(heroRepository.findByUser(user)).thenReturn(Optional.of(hero));

        when(heroMapper.toHeroDTOResponse(hero)).thenReturn(
                HeroDTOResponse.builder()
                        .id(1L)
                        .heroClass("Barbarian")
                        .health(12)
                        .attack(14)
                        .defense(8)
                        .movement(6)
                        .experiencia(0)
                        .nivel(1)
                        .build()
        );

        HeroDTOResponse response = heroService.getHeroByUser(user);

        assertNotNull(response);
        assertEquals("Barbarian", response.getHeroClass());
    }

    @Test
    void getHeroByUser_ShouldThrowException_WhenNoHeroFound() {
        when(heroRepository.findByUser(user)).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> heroService.getHeroByUser(user));
        assertEquals("This user has no hero assigned", ex.getMessage());
    }

    @Test
    void updateHeroStats_ShouldUpdateAndReturnHeroDTO() {
        HeroUpdateStatsDTO statsDTO = new HeroUpdateStatsDTO();
        statsDTO.setHealth(20);
        statsDTO.setDefense(15);
        statsDTO.setAttack(25);
        statsDTO.setMovement(7);
        // No usamos puntosRestantes en el DTO de salida

        when(heroRepository.findByUser(user)).thenReturn(Optional.of(hero));
        when(heroRepository.save(hero)).thenReturn(hero);

        when(heroMapper.toHeroDTOResponse(hero)).thenReturn(
                HeroDTOResponse.builder()
                        .id(1L)
                        .heroClass("Barbarian")
                        .health(20)
                        .attack(25)
                        .defense(15)
                        .movement(7)
                        .experiencia(0)
                        .nivel(1)
                        .build()
        );

        HeroDTOResponse response = heroService.updateHeroStats(user, statsDTO);

        assertNotNull(response);
        assertEquals(20, response.getHealth());
        assertEquals(25, response.getAttack());
        assertEquals(15, response.getDefense());
        assertEquals(7, response.getMovement());
        verify(heroRepository).save(hero);
    }

    @Test
    void updateHeroStats_ShouldThrowException_WhenNoHeroFound() {
        HeroUpdateStatsDTO statsDTO = new HeroUpdateStatsDTO();
        when(heroRepository.findByUser(user)).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> heroService.updateHeroStats(user, statsDTO));
        assertEquals("This user has no hero assigned", ex.getMessage());
    }
}