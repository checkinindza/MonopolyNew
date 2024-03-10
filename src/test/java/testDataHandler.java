class testDataHandler {
    @Test
    void dataIsLoadedSuccesfully() {
        DataHandler data = new DataHandler();
        Boolean loaded = data.isDataLoaded();
        assertTrue(loaded == true, () -> "Data was not loaded");
    }
}
