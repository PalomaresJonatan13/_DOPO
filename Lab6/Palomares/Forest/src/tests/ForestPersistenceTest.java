package tests;

import domain.*;
import domain.forestFire.Land;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ForestPersistenceTest {

    private static File getFile(String name) {
        return new File("src/tests/filesForPersistenceTests/" + name);
    }

    private static final File FILE1_DAT = getFile("file1.dat");
    private static final File NOT_FOREST_DAT = getFile("notForest.dat");
    private static final File FILE1_TXT = getFile("file1.txt");
    private static final File EMPTY_DAT = getFile("empty.dat");
    private static final File EMPTY_TXT = getFile("empty.txt");

    private Forest forest;

    @Before
    public void setup() {
        forest = new Forest();
    }

    @After
    public void tearDown() {
        clearToEmptyFile(EMPTY_DAT);
        clearToEmptyFile(EMPTY_TXT);
    }

    /**
     * Leaves the file with zero bytes (no content, no lines).
     */
    private static void clearToEmptyFile(File file) {
        try {
            Files.write(file.toPath(), new byte[0]);
        } catch (IOException e) {
            fail("Could not reset " + file.getPath() + ": " + e.getMessage());
        }
    }

    @Test
    public void shouldNotSaveAsAFileWithWrongExtension() {
        try {
            forest.saveAs(new File("wrong.bin"));
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldSaveAsAFileWithCorrectExtension() {
        try {
            forest.saveAs(EMPTY_DAT);
            assertTrue(EMPTY_DAT.exists());
            assertTrue(Files.size(EMPTY_DAT.toPath()) > 0);
        } catch (ForestException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldNotOpenAFileWithWrongExtension() {
        try {
            forest.openFile(new File("notes.txt"));
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldOpenAFileWithCorrectExtension() {
        try {
            Forest loaded = forest.openFile(FILE1_DAT);
            assertTrue(loaded.getThing(5, 5) instanceof Tree);
            assertTrue(loaded.getThing(0, 0) instanceof Land);
        } catch (ForestException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldNotExportAsAFileWithWrongExtension() {
        try {
            forest.exportAs(new File("out.dat"));
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldExportAsAFileWithCorrectExtension() {
        try {
            Forest small = new Forest(true);
            new Tree(small, 7, 8);
            small.exportAs(EMPTY_TXT);
            List<String> lines = Files.readAllLines(EMPTY_TXT.toPath());
            assertEquals(1, lines.size());
            assertEquals("Tree 7,8", lines.get(0));
        } catch (ForestException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldNotImportFileWithWrongExtension() {
        try {
            forest.importFile(FILE1_DAT);
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldImportFileWithCorrectExtension() {
        try {
            Forest empty = new Forest(true);
            empty.importFile(FILE1_TXT);
            assertTrue(empty.getThing(5, 5) instanceof Tree);
            assertTrue(empty.getThing(0, 0) instanceof Land);
        } catch (ForestException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldIncludeCauseAndDetailsWhenOpenFileFails() {
        try {
            forest.openFile(NOT_FOREST_DAT);
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getCause());
            String msg = e.getMessage();
            assertTrue(msg.contains("acción=Abrir"));
            assertTrue(msg.contains(NOT_FOREST_DAT.getAbsolutePath()));
            assertTrue(msg.contains(e.getCause().getClass().getName()));
        }
    }

    @Test
    public void shouldIncludeCauseAndDetailsWhenSaveAsFails() {
        File tmp = new File(System.getProperty("java.io.tmpdir"));
        File missingParent = new File(new File(tmp, "forest_missing_" + System.nanoTime()), "nested");
        File unreachable = new File(missingParent, "out.dat");
        try {
            forest.saveAs(unreachable);
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getCause());
            String msg = e.getMessage();
            assertTrue(msg.contains("acción=Guardar"));
            assertTrue(msg.contains(unreachable.getAbsolutePath()));
            assertTrue(msg.contains(e.getCause().getClass().getName()));
        }
    }

    @Test
    public void shouldIncludeCauseAndDetailsWhenImportFileFails() {
        File missing = new File(
            System.getProperty("java.io.tmpdir"),
            "forest_import_missing_" + System.nanoTime() + ".txt");
        try {
            forest.importFile(missing);
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getCause());
            String msg = e.getMessage();
            assertTrue(msg.contains("acción=Importar"));
            assertTrue(msg.contains(missing.getAbsolutePath()));
            assertTrue(msg.contains(e.getCause().getClass().getName()));
        }
    }

    @Test
    public void shouldIncludeCauseAndDetailsWhenExportAsFails() {
        File tmp = new File(System.getProperty("java.io.tmpdir"));
        File missingParent = new File(new File(tmp, "forest_export_missing_" + System.nanoTime()), "nested");
        File unreachable = new File(missingParent, "out.txt");
        try {
            forest.exportAs(unreachable);
            fail("Expected ForestException");
        } catch (ForestException e) {
            assertNotNull(e.getCause());
            String msg = e.getMessage();
            assertTrue(msg.contains("acción=Exportar"));
            assertTrue(msg.contains(unreachable.getAbsolutePath()));
            assertTrue(msg.contains(e.getCause().getClass().getName()));
        }
    }
}
