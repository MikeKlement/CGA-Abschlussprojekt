package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import terrains.TerrainTexturePack;
import textures.ModelTexture;
import textures.TerrainTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//********** TERRAIN TEXTURE STUFF *******************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassy3"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
	
		//Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap);
		//Terrain terrain2 = new Terrain(-1,-1,loader,texturePack, blendMap);
		
		RawModel model = OBJLoader.loadObjModel("tree", loader);
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
		
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));

		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
	//	fernTexture.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTexture);

	//	TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));

		grass.getTexture().setHasTransparency(true);
	//	grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
	//	flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);	
			
		//RawModel model1 = OBJLoader.loadObjModel("treegreen", loader);
		//TexturedModel staticModel1 = new TexturedModel(model1,new ModelTexture(loader.loadTexture("treegreen")));
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for(int i=0;i<500;i++){
			entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
		}

		
		Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		
		
		Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1, -1,loader,texturePack,blendMap); 
		
	
		//List<Terrain> terrain = new ArrayList<Terrain>();
		//Terrain terrain1 = new Terrain(0, -1, loader, texturePack, blendMap); 	// darker the spot the lower the spot
		//Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap); 
		
		
		
		//List<GuiTexture> guis = new ArrayList <GuiTexture>();
		//GuiTexture gui = new GuiTexture(loader.loadTexture(""),new Vector2f (0.5f, 0.5f), new Vector2f (0.25f, 0.25f));
		//guis.add(gui);
		
		
		RawModel playerTexture = OBJLoader.loadObjModel("player", loader);
		TexturedModel playerTexturedModel = new TexturedModel(playerTexture, new ModelTexture(loader.loadTexture("white")));

	//	TextureModel grass = new TexturedModel(OBJLoader.loadObjModel("grassmodel", loader),
		//		newModelTexture(loader.loadTexture
		
		Player player = new Player(playerTexturedModel, new Vector3f(100, 5, -150), 0, 180, 0, 0.6f);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		Camera camera = new Camera(player);	
		MasterRenderer renderer = new MasterRenderer(loader);
		
		while(!Display.isCloseRequested()){
			camera.move();
			player.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			guiRenderer.cleanUp();
			renderer.render(light, camera);
			//guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}