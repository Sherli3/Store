package store.configuration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import store.configuration.model.Game;
import store.configuration.repository.GameRepository;
import store.configuration.service.impl.GameServiceImpl;

public class GameServiceTest {
	private Game game;
	@Mock
	private GameRepository gameRepository;
	@InjectMocks
	private GameServiceImpl service;

	@Before
	public void before() {
		game = new Game();
		int fakeId = 100500;
		game.setId((int) fakeId);
		game.setGameName("FakeGame100500");
	}

	@Test
	public void testAddGame() {
		game.setGameName(null);
		ArgumentCaptor<Game> decoCaptor = ArgumentCaptor.forClass(Game.class);
		when(gameRepository.save(decoCaptor.capture()));
		service.saveGame(game);
		assertEquals(100500, decoCaptor.getValue().getId());
		assertEquals("FakeGame100500", decoCaptor.getValue().getGameName().toString());
	}

}
