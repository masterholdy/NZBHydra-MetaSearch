## Run
```$ java -jar NfoMetaSearch.jar (--server.port={port})```

### Example
```$ java -jar NfoMetaSearch.jar``` Default Port `8080`

```$ java -jar NfoMetaSearch.jar --server.port=4747```

## Add to NZBHydra
Add Indexer &rightarrow;  add custom Newznab Indexer &rightarrow; 

`Host: http://{HOST}:{PORT}/` &rightarrow;  `Host: http://localhost:8080/`

`Priority: {x}` &rightarrow; `Priority: -10` &rightarrow; small Number &rightarrow; Real-Indexers should have higher Prio

`Enable for: Internal searches only` 

## Add to Autostart
```echo start javaw.exe -jar "{fullPathTo-JAR-FILE}" --server.port={PORT} > "%userprofile%\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup\autostart-nfo-metasearch.bat" ```
