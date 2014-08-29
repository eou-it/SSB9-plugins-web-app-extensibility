dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
    //show_sql = true
}
// environment specific settings
environments {
    development {

        //Banner database (default)
        dataSource {
            pooled = true
            driverClassName = "oracle.jdbc.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "baninst1"
            password = "u_pick_it"
            url = "jdbc:oracle:thin:@oracledb:1521:ban83"
            dbCreate = "none" //"validate"
            //loggingSql = true
            //logSql =true
        }
	}
    test {
        //Banner database (default)
        dataSource {
            pooled = true
            driverClassName = "oracle.jdbc.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "baninst1"
            password = "u_pick_it"
            url = "jdbc:oracle:thin:@oracledb:1521:ban83"
            dbCreate = "none" //"validate"
            //loggingSql = true
            //logSql =true
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE"
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=true
                validationQuery="SELECT 1"
            }
        }
    }
}
