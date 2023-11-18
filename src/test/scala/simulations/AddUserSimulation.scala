package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AddUserSimulation extends Simulation {

  val httpConf = http.baseUrl(url = "https://reqres.in/")
    .header(name = "Accept", value = "application/json")
    .header(name = "content-type", value = "application/json")

  val scn = scenario(scenarioName = "Add Users Scenario")
    .exec(http(requestName = "add user request")
    .get(url = "/api/users")
    .body(RawFileBody(filePath = "./src/test/resources/bodies/AddUser.json")).asJson
      .header(name = "content-type", value = "application/json")
      .check(status is 201)
      .check(responseTimeInMillis.lte(100)))


    .pause(duration = 3)

    .exec(http(requestName = "get user request")
      .get("/api/users/2")
      .check(status is 200))

    .pause(duration = 2)

    .exec(http(requestName = "get all user request")
      .get("/api/users?page=2")
      .check(status is 200))

  setUp(scn.inject(atOnceUsers(users = 10))).protocols(httpConf)
}
