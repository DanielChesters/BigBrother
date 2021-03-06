package me.taylorkelly.bigbrother.fixes;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.taylorkelly.bigbrother.BBSettings;
import me.taylorkelly.bigbrother.BigBrother;
import me.taylorkelly.bigbrother.datasource.ConnectionManager;

public class Fix14 extends Fix {
    public Fix14(File dataFolder) {
        super(dataFolder);
    }

    protected int version = 2;
    public static final String[] UPDATE_SQLITE = {
            "CREATE TEMPORARY TABLE bbdata_backup(id, date, player, action, world, x, y, z, type, data, rbacked);",
            "INSERT INTO bbdata_backup SELECT id, date, player, action, world, x, y, z, type, data, rbacked FROM bbdata;",
            "DROP TABLE bbdata;",
            "CREATE TABLE `bbdata` (`id` INTEGER PRIMARY KEY,`date` INT UNSIGNED NOT NULL DEFAULT '0',`player` varchar(32) NOT NULL DEFAULT 'Player',`action` tinyint NOT NULL DEFAULT '0',`world` tinyint NOT NULL DEFAULT '0',`x` int NOT NULL DEFAULT '0',`y` tinyint UNSIGNED NOT NULL DEFAULT '0',`z` int NOT NULL DEFAULT '0',`type` smallint NOT NULL DEFAULT '0',`data` varchar(150) NOT NULL DEFAULT '',`rbacked` boolean NOT NULL DEFAULT '0');",
            "INSERT INTO bbdata SELECT id, date, player, action, world, x, y, z, type, data, rbacked FROM bbdata_backup;", "DROP TABLE bbdata_backup;" };
    public static final String UPDATE_MYSQL = "ALTER TABLE bbdata MODIFY y tinyint UNSIGNED";

    public void apply() {
        if (needsUpdate(version)) {
            Logger log = Logger.getLogger("Minecraft");
            log.info("[BBROTHER] Updating table for 1.4");
            boolean sqlite = !BBSettings.mysql;

            if (updateTable(sqlite)) {
                updateVersion(version);
            }
        }
    }

    private static boolean updateTable(boolean sqlite) {
        Connection conn = null;
        Statement st = null;
        try {
            conn = ConnectionManager.getConnection();
            st = conn.createStatement();
            if (sqlite) {
                for (int i = 0; i < UPDATE_SQLITE.length; i++) {
                    st.executeUpdate(UPDATE_SQLITE[i]);
                }
            } else {
                st.executeUpdate(UPDATE_MYSQL);
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            BigBrother.log.log(Level.SEVERE, "[BBROTHER]: Update Table 1.4 Fail " + ((sqlite) ? " sqlite" : " mysql"), e);
            return false;
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                BigBrother.log.log(Level.SEVERE, "[BBROTHER]: Update Table 1.4 Fail (on close)");
            }
        }
    }

}
