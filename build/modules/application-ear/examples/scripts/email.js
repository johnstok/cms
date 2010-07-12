var ccc = Packages.ccc;
 
mail.send(
    new ccc.api.types.EmailAddress("to@civicuk.com"),
    new ccc.api.types.EmailAddress("from@example.com"),
    "Subject",
    "Body");

print("done");

