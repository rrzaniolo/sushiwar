
package sprite;

/* @author Hossomi
 * 
 * CLASS Animation ----------------------------------------
 * Contém controle de animações, como frame inicial, frame
 * final, quantidade de frames e duração.
 */

public class Animation {

	/**
	 * Cria uma nova sequência padronizada não repetitiva. Todos os quadros
	 * terão mesma duração.
	 * 
	 * @param name Nome da animação.
	 * @param frameStart Quadro de início da animação (no spritesheet)
	 * @param frameCount Quantidade de quadros.
	 * @param defaultPeriod Duração de cada quadro.
	 */
	public Animation( String name, int frameStart, int frameCount, int defaultPeriod ) {
	 
		this.frameStart = frameStart;
		this.frameCount = frameCount;
		this.name = name;
		
		framePeriod = new int[frameCount];
		
		for (int i=0; i<frameCount; i++)
			framePeriod[i] = defaultPeriod;

	}
	
	/**
	 * Cria uma nova sequência padronizada. Todos os quadros
	 * terão mesma duração.
	 * 
	 * @param name Nome da animação.
	 * @param frameStart Quadro de início da animação (no spritesheet)
	 * @param frameCount Quantidade de quadros.
	 * @param defaultPeriod Duração de cada quadro.
	 * @param looping Se vedadeiro, a animação retornará ao primeiro frame
	 * quando terminar.
	 */
	public Animation( String name, int frameStart, int frameCount, int defaultPeriod, boolean looping ) {
	 
		this.frameStart = frameStart;
		this.frameCount = frameCount;
		this.name = name;
		this.looping = looping;
		
		framePeriod = new int[frameCount];
		
		for (int i=0; i<frameCount; i++) 
			framePeriod[i] = defaultPeriod;
		
	}
	
	/**
	 * Cria uma nova sequência padronizada. Todos os quadros
	 * terão mesma duração.
	 * 
	 * @param name Nome da animação.
	 * @param frameStart Quadro de início da animação (no spritesheet)
	 * @param frameCount Quantidade de quadros.
	 * @param defaultPeriod Duração de cada quadro.
	 * @param looping Se vedadeiro, a animação retornará para o quadro
	 * 'loopReturn' quando terminar.
	 */
	public Animation( String name, int frameStart, int frameCount, int defaultPeriod, boolean looping, int loopReturn ) {
	 
		this.frameStart = frameStart;
		this.frameCount = frameCount;
		this.name = name;
		this.looping = looping;
		this.loopReturn = loopReturn;
		
		framePeriod = new int[frameCount];
		
		for (int i=0; i<frameCount; i++) 
			framePeriod[i] = defaultPeriod;
		
	}
	
	//	--	Manipulação  --
	
	/**
	 * Define em qual frame a animação está.
	 * @param frame O frame para onde a animação deve ir.
	 */
	/*private void setFrame( int frame ) {
		frameNowIndex = frame;
		frameNow = frameStart + frame;
		timeCount = framePeriod[ frameNowIndex ];
	}*/
	
	/**
	 * Define a duração de um frame específico.
	 * @param frame O frame a modificar.
	 * @param period A duração do frame.
	 */
	public void setFramePeriod( int frame, int period ) {
		if (frame < frameCount && period > 0)
			framePeriod[frame] = period;
		
	}
	
	/**
	 * Reinicia a animação.
	 */
	/*public void start() {
		frameNowIndex = 0;
		frameNow = frameStart;
		timeCount = framePeriod[0];
	}*/
	
	/**
	 * Decrementa o contador de tempo segundo o FPS
	 * padrão, retornando informações sobre o estado
	 * da animação.
	 */
	/*public boolean handle() {
		
		timeCount -= timerPeriod;
		
		if (timeCount <= 0) {
			
			if (frameNowIndex >= frameCount-1) {
				if (looping)
					setFrame(loopReturn);

				return true;
			}

			else
				setFrame( frameNowIndex + 1 );
		
		}
		
		return false;
	}*/
	
	//	--  Acesso  --
	/**
	 * Retorna o nome da animação.
	 * @return O nome da animação.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retorna o quadro atual.
	 * @return O quadro atual.
	 */
	//public int getCurrentFrame() {
	//	return frameNow;
	//}
	
	/**
	 * Retorna o quadro inicial.
	 * @return O quadro inicial.
	 */
	public int getStartFrame() {
		return frameStart;
	}
	
	/**
	 * Retorna o número de quadros.
	 * @return O número de quadros.
	 */
	public int getFrameCount() {
		return frameCount;
	}
	
	public int getFramePeriod( int i ) {
		return framePeriod[i];
	}
	
	public int getNextFrame( int now ) {
		if (now >= frameCount-1) {
			if (looping)
				return loopReturn;

			return now;
		}

		return now + 1;
		
	}
	
	/**
	 * Retorna o quadro a retornar quando terminar
	 * a animação.
	 * @return O quadro a retornar.
	 */
	public int getLoopReturn() {
		return loopReturn;
	}
	
	public boolean isLooping() {
		return looping;
	}
	
	//	--	Dados específicos  --
	private int frameStart = 0;
	private int frameCount = 0;
	private int []framePeriod = null;
	private boolean looping = false;
	private int loopReturn = 0;
	private String name = null;
	//private int timerPeriod = Sprite.timerPeriod;
	
	//	--  Dados de controle  --
	//private int frameNow;
	//private int frameNowIndex;
	//private int timeCount;
	
}
