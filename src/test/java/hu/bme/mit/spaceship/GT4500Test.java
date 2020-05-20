package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimaryTorpedoStore;
  private TorpedoStore mockSecondaryTorpedoStore;

  @BeforeEach
  public void init(){
    mockPrimaryTorpedoStore = mock(TorpedoStore.class);
    mockSecondaryTorpedoStore = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimaryTorpedoStore, mockSecondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_Alternating(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    InOrder inOrder = inOrder(mockPrimaryTorpedoStore, mockSecondaryTorpedoStore);
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    inOrder.verify(mockPrimaryTorpedoStore).fire(1);
    inOrder.verify(mockSecondaryTorpedoStore).fire(1);
    assertEquals(true, result1);
    assertEquals(true, result2);
  }

  @Test
  public void fireTorpedo_Single_PrimaryEmpty(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_SecondaryEmpty(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_PrimaryFails(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, never()).isEmpty();
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Single_PrimaryOneSecondaryZero(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false, true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(0)).fire(1);
    assertEquals(true, result1);
    assertEquals(false, result2);
  }

  @Test
  public void fireTorpedo_Single_PrimaryZeroSecondaryOne(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false, true);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(0)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result1);
    assertEquals(false, result2);
  }

  @Test
  public void fireTorpedo_Single_AlternateSecondaryOne(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorpedoStore, times(2)).fire(1);
    verify(mockSecondaryTorpedoStore, times(0)).fire(1);
    assertEquals(true, result1);
    assertEquals(true, result2);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_PrimaryEmpty(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_SecondaryEmpty(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_SecondaryFails(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_AllFails(){
    // Arrange
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(false);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    assertEquals(false, result);
  }
}
