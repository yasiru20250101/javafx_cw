package com.example.courswork.parser;

import com.example.courswork.model.Dealer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealerParserTest {

    @TempDir
    Path tempDir;

    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = tempDir.resolve("dealers_test.txt").toFile();

        String dirtyContent =
                "D101, Sunil Motors, 0771234567, Malabe\n" +
                        "D102|Kaduwela Spares Hub|0719876543|Kaduwela\n" +
                        "D103; Ranatunga Auto; ; Pittugala\n" +
                        "BAD_DEALER_LINE\n";

        Files.write(tempFile.toPath(), dirtyContent.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testParseDealersWithDirtyData() {
        List<Dealer> dealers = dealerParser.parseDealers(tempFile.getAbsolutePath());

        assertEquals(3, dealers.size());
        assertEquals("D101", dealers.get(0).getDealerId());
        assertEquals("Sunil Motors", dealers.get(0).getDealerName());

        assertEquals("D102", dealers.get(1).getDealerId());

        assertEquals("D103", dealers.get(2).getDealerId());
        assertEquals("", dealers.get(2).getDealerPhone());
        assertEquals("Pittugala", dealers.get(2).getDealerLocation());
    }
}