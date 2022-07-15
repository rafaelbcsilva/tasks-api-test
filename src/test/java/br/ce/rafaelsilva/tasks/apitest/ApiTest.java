package br.ce.rafaelsilva.tasks.apitest;

import org.graalvm.compiler.lir.amd64.vector.AMD64VectorShuffle.Extract128Op;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ApiTest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	@Test
	public void deveRetornarTarefas() {
		RestAssured.given().when().get("/todo").then().statusCode(200);
	}

	@Test
	public void deveAdicionarTarefasComSucesso() {
		RestAssured.given().body("{\"task\": \"Teste Via API\",\"dueDate\": \"2022-09-25\" }")
				.contentType(ContentType.JSON).when().post("/todo").then()

				.statusCode(201);

	}
	
	@Test
	public void NãodeveAdicionarTarefaInvalida() {
		RestAssured.given().body("{\"task\": \"Teste Via API\",\"dueDate\": \"2020-05-11\" }")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.log().all()
				.statusCode(400)
				.body("message",CoreMatchers.is("Due date must not be in past"));

	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		//inserir
		Integer id= RestAssured.given().body("{\"task\": \"Tarefa para Remoção\",\"dueDate\": \"2022-09-25\" }")
					.contentType(ContentType.JSON)
				.when()
					.post("/todo")
				.then()
			//		.log().all()
					.statusCode(201)
					.extract().path("id");
		
		System.out.println(id);
		
		//remover
		RestAssured.given()
		.when()
		 	.delete("/todo/"+ id)
		.then()
		 	.statusCode(204)
		 	;
	}
	
}
