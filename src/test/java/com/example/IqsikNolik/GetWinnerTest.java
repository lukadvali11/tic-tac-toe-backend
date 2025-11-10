package com.example.IqsikNolik;

import com.example.IqsikNolik.domain.Symbol;
import com.example.IqsikNolik.service.GameStateService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.example.IqsikNolik.domain.Symbol.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GetWinnerTest {

    @ParameterizedTest
    @MethodSource("provideArguments")
    public void shouldGetCorrectWinner(String boardState, Symbol symbol) {
        Symbol result = GameStateService.getWinner(boardState);
        assertEquals(symbol, result);
    }

    public static Stream<Arguments> provideArguments() {
        return Stream.of(
                Arguments.of("OXOOXXXXO", X),
                Arguments.of("XXOXONONN", O),
                Arguments.of("OOXXXOOXX", N),
                Arguments.of("NXNNOXNNN", N),
                Arguments.of("NNNNNNNNN", N),
                Arguments.of("NXNOOOXXN", O)
        );
    }
}
