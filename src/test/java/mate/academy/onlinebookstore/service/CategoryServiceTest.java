package mate.academy.onlinebookstore.service;

import static mate.academy.onlinebookstore.util.TestUtils.createCategoryRequestDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mate.academy.onlinebookstore.dto.CategoryRequestDto;
import mate.academy.onlinebookstore.dto.CategoryResponseDto;
import mate.academy.onlinebookstore.entity.Category;
import mate.academy.onlinebookstore.mapper.CategoryMapper;
import mate.academy.onlinebookstore.repository.CategoryRepository;
import mate.academy.onlinebookstore.service.impl.CategoryServiceImpl;
import mate.academy.onlinebookstore.util.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("""
        Test findAll() method. It returns list of all categories
            """)
    void findAll_validPageable_returnsAllCategories() {
        //Given
        Category category1 = TestUtils.createFirstCategory();
        Category category2 = TestUtils.createSecondCategory();
        CategoryResponseDto categoryResponseDto1 = TestUtils.categoryToResponseDto(category1);
        CategoryResponseDto categoryResponseDto2 = TestUtils.categoryToResponseDto(category2);
        List<Category> expected = List.of(category1, category2);
        List<CategoryResponseDto> expectedDto = List.of(categoryResponseDto1, categoryResponseDto2);
        Pageable pageable = PageRequest.of(0, 5);

        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(expected));
        when(categoryMapper.toDto(category1)).thenReturn(categoryResponseDto1);

        //When
        List<CategoryResponseDto> actual = categoryService.findAll(pageable);

        //Then
        assertEquals(expectedDto.get(0), actual.get(0));
    }

    @Test
    @DisplayName("""
        Test getById() method. It returns one category
            """)
    void getById_validId_returnsOneCategory() {
        //Given
        Category category1 = TestUtils.createFirstCategory();
        CategoryResponseDto expectedDto = TestUtils.categoryToResponseDto(category1);
        when(categoryRepository.findById(TestUtils.FIRST_MOCK_ID))
                .thenReturn(Optional.of(category1));
        when(categoryMapper.toDto(category1)).thenReturn(expectedDto);

        //When
        CategoryResponseDto actual = categoryService.getById(TestUtils.FIRST_MOCK_ID);

        //Then
        assertEquals(expectedDto.id(), actual.id());
    }

    @Test
    @DisplayName("""
        Test update() method. It updates one category
            """)
    public void update_validId_returnsUpdateOneCategory() {
        //Given
        Category editedCategory = TestUtils.createFirstCategory();
        CategoryResponseDto editedCategoryDto = TestUtils.categoryToResponseDto(editedCategory);
        editedCategory.setDescription(editedCategoryDto.description());
        CategoryRequestDto editedDto = createCategoryRequestDto();
        when(categoryRepository.findById(TestUtils.FIRST_MOCK_ID))
                .thenReturn(Optional.of(editedCategory));
        when(categoryRepository.save(editedCategory)).thenReturn(editedCategory);
        when(categoryMapper.toDto(editedCategory)).thenReturn(editedCategoryDto);

        //When
        CategoryResponseDto actual = categoryService.update(editedCategory.getId(), editedDto);

        //Then
        assertEquals(editedCategory.getName(), actual.name());
    }

    @Test
    @DisplayName("""
        Test deleteById() method. It deletes one category by id
            """)
    public void deleteById_validId_ok() {
        //Given
        doNothing().when(categoryRepository).deleteById(TestUtils.FIRST_MOCK_ID);

        //When
        categoryService.deleteById(TestUtils.FIRST_MOCK_ID);

        //Then
        verify(categoryRepository, times(1)).deleteById(TestUtils.FIRST_MOCK_ID);
    }

    @Test
    @DisplayName("""
            Test save(). It tests create new category, "
            + "add category to database and return CategoryResponseDto
                """)
    public void save_checkValidData_createOneCategory() {
        //Given
        Category category = TestUtils.createFirstCategory();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        //When
        List<CategoryResponseDto> categoryDtos = categoryService.findAll(pageable);

        //Then
        assertThat(categoryDtos, hasSize(1));
    }

    @Test
    void deleteProductWithExceptionTest() {
        doThrow(RuntimeException.class).when(categoryRepository).deleteById(any());

        assertThrows(RuntimeException.class,
                () -> categoryService.deleteById(TestUtils.SECOND_MOCK_ID));
    }
}
